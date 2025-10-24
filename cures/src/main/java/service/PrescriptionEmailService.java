package service;

import util.Constant;
import util.EnDeCryptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PrescriptionEmailService (plain javax.mail, no JavaMailSender)
 *
 * Uses javax.mail.Session + Transport. Decrypts password at startup.
 */
@Service
public class PrescriptionEmailService {

	private static final Logger LOGGER = Logger.getLogger(PrescriptionEmailService.class.getName());

	// Primary config - can map these from application.properties or keep them in
	// cures.properties
	@Value("${mail.host:}")
	private String smtpHost;

	@Value("${mail.port:587}")
	private String smtpPort;

	@Value("${mail.username:}")
	private String smtpUsername;

	// Either provide encrypted password or plain password
	@Value("${mail.password.encrypted:}")
	private String smtpPasswordEncrypted;

	@Value("${mail.password:}")
	private String smtpPasswordPlain;

	@Value("${mail.from:}")
	private String smtpFrom;

	@Value("${mail.ssl.enable:false}")
	private boolean smtpSslEnable;

	@Value("${mail.starttls.enable:true}")
	private boolean smtpStartTlsEnable;

	// Fallback to a properties file on classpath if needed (optional)
	@Value("${mail.legacy.props:cures.properties}")
	private String legacyPropsName;

	// Retry config
	@Value("${mail.retry.attempts:3}")
	private int retryAttempts;

	@Value("${mail.retry.backoff.ms:1000}")
	private long retryBackoffMs;

	// Internal decrypted password
	private String smtpPassword;

	// Executor for optional async sending
	private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
		Thread t = new Thread(r, "PrescriptionEmailService-Sender");
		t.setDaemon(true);
		return t;
	});

	@PostConstruct
	public void init() {
		try {
			// If core properties are not provided via @Value, try loading legacy props file
			// from classpath
			if ((smtpHost == null || smtpHost.isBlank()) || (smtpUsername == null || smtpUsername.isBlank())) {
				loadLegacyPropertiesIfPresent();
			}

			// decrypt password once
			if (smtpPasswordEncrypted != null && !smtpPasswordEncrypted.isBlank()) {
				EnDeCryptor enc = new EnDeCryptor();
				smtpPassword = enc.decrypt(smtpPasswordEncrypted, Constant.SECRETE);
			} else {
				smtpPassword = smtpPasswordPlain;
			}

			LOGGER.info(String.format("PrescriptionEmailService initialized (host=%s, port=%s, from=%s)", smtpHost,
					smtpPort, smtpFrom));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to initialize PrescriptionEmailService", e);
		}
	}

	private void loadLegacyPropertiesIfPresent() {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(legacyPropsName)) {
			if (in == null) {
				LOGGER.fine(legacyPropsName + " not found on classpath; skipping legacy load.");
				return;
			}
			Properties p = new Properties();
			p.load(in);

			if ((smtpHost == null || smtpHost.isBlank()) && p.getProperty("SMTP_HOST") != null) {
				smtpHost = p.getProperty("SMTP_HOST");
			}
			if ((smtpPort == null || smtpPort.isBlank()) && p.getProperty("SMTP_PORT") != null) {
				smtpPort = p.getProperty("SMTP_PORT");
			}
			if ((smtpUsername == null || smtpUsername.isBlank()) && p.getProperty("SMTP_EMAIL_FROM_INFO") != null) {
				smtpUsername = p.getProperty("SMTP_EMAIL_FROM_INFO");
				smtpFrom = smtpFrom == null || smtpFrom.isBlank() ? smtpUsername : smtpFrom;
			}
			if ((smtpPasswordEncrypted == null || smtpPasswordEncrypted.isBlank())
					&& p.getProperty("SMTP_EMAIL_PASS_ENC") != null) {
				smtpPasswordEncrypted = p.getProperty("SMTP_EMAIL_PASS_ENC");
			} else if ((smtpPasswordPlain == null || smtpPasswordPlain.isBlank())
					&& p.getProperty("SMTP_EMAIL_PASS") != null) {
				smtpPasswordPlain = p.getProperty("SMTP_EMAIL_PASS");
			}
			if (p.getProperty("SMTP_SSL_ENABLE") != null) {
				smtpSslEnable = "true".equalsIgnoreCase(p.getProperty("SMTP_SSL_ENABLE"));
			}
			if (p.getProperty("SMTP_STARTTLS_ENABLE") != null) {
				smtpStartTlsEnable = "true".equalsIgnoreCase(p.getProperty("SMTP_STARTTLS_ENABLE"));
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error loading legacy properties from " + legacyPropsName, e);
		}
	}

	/**
	 * Synchronous send using javax.mail.Session + Transport with simple
	 * retry/backoff.
	 *
	 * @return true if message successfully sent (or attempted without unrecoverable
	 *         exception)
	 */
	public boolean sendPrescriptionEmail(String patientEmail, String patientName, String doctorName,String followUpIso, String notes,
			String attachmentPath, String originalFilename) {
		if (patientEmail == null || patientEmail.isBlank()) {
			LOGGER.warning("No patient email provided; abort sending.");
			return false;
		}
		int attempt = 0;
		while (attempt < Math.max(1, retryAttempts)) {
			attempt++;
			try {
				Session session = createSession();
				// build message
				MimeMessage message = new MimeMessage(session);
				String fromAddr = (smtpFrom != null && !smtpFrom.isBlank()) ? smtpFrom : smtpUsername;
				message.setFrom(new InternetAddress(fromAddr,"All-Cures Healthcare Team"));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(patientEmail));
				message.setSubject("Your Prescription from All-Cures Healthcare Platform", "UTF-8");


				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(buildHtmlBody(patientName, doctorName,followUpIso, notes), "text/html; charset=utf-8");

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(htmlPart);

				// attachment
				if (attachmentPath != null && !attachmentPath.isBlank()) {
					File f = new File(attachmentPath);
					if (f.exists() && f.isFile() && f.canRead()) {
						MimeBodyPart attachPart = new MimeBodyPart();
						attachPart.attachFile(f);
						String fname = (originalFilename != null && !originalFilename.isBlank()) ? originalFilename
								: f.getName();
						attachPart.setFileName(MimeUtility.encodeText(fname, "UTF-8", null));
						multipart.addBodyPart(attachPart);
					} else {
						LOGGER.warning("Attachment file not found or unreadable: " + attachmentPath);
					}
				}

				message.setContent(multipart);

				// Send using Transport; this respects session auth properties.
				Transport.send(message);
				LOGGER.info("Prescription email sent to " + patientEmail );
				return true;
			} catch (SendFailedException sfe) {
				// hard failure (address invalid etc.) - don't retry
				LOGGER.log(Level.SEVERE, "SendFailedException (will not retry): attempt " + attempt, sfe);
				return false;
			} catch (MessagingException me) {
				LOGGER.log(Level.WARNING, "MessagingException on attempt " + attempt + " of " + retryAttempts, me);
				if (attempt >= retryAttempts) {
					LOGGER.log(Level.SEVERE, "Exceeded retry attempts sending prescription email", me);
					return false;
				} else {
					try {
						Thread.sleep(retryBackoffMs * attempt); // simple incremental backoff
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						return false;
					}
				}
			} catch (Exception ex) {
				LOGGER.log(Level.SEVERE, "Unexpected exception when sending prescription email", ex);
				return false;
			}
		}
		return false;
	}

	/**
	 * Non-blocking helper returning a Future. Uses internal executor. Caller can
	 * call get() if they need to block — otherwise it's fire-and-forget.
	 */
	public Future<?> sendPrescriptionEmailAsync(String patientEmail, String patientName, String doctorName,String followUpIso, String notes, String attachmentPath, String originalFilename) {
		return executor.submit(() -> sendPrescriptionEmail(patientEmail,patientName,doctorName,followUpIso, notes,
				attachmentPath, originalFilename));
	}

	private Session createSession() {
		Properties props = new Properties();
		if (smtpHost != null && !smtpHost.isBlank())
			props.put("mail.smtp.host", smtpHost);
		if (smtpPort != null && !smtpPort.isBlank())
			props.put("mail.smtp.port", smtpPort);
		props.put("mail.smtp.auth", smtpUsername != null && !smtpUsername.isBlank() ? "true" : "false");
		props.put("mail.smtp.starttls.enable", String.valueOf(smtpStartTlsEnable));
		props.put("mail.smtp.ssl.enable", String.valueOf(smtpSslEnable));

		// reasonable timeouts (ms)
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.timeout", "10000");
		props.put("mail.smtp.writetimeout", "10000");

		// Use username/password if present
		if (smtpUsername != null && !smtpUsername.isBlank() && smtpPassword != null) {
			return Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUsername, smtpPassword);
				}
			});
		} else {
			return Session.getInstance(props);
		}
	}

	private String buildHtmlBody(String patientName, String doctorName,String followUpIso, String notes) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>");

	    sb.append("<p>Dear ").append(escapeHtml(patientName != null ? patientName : "Patient")).append(",</p>");
	    sb.append("<p>We hope this message finds you well.</p>");

	    sb.append("<p>Your doctor, <strong>")
	      .append(escapeHtml(doctorName != null ? doctorName : ""))
	      .append("</strong>, has prepared and uploaded your prescription.</p>");

	    if (notes != null && !notes.isBlank()) {
	        sb.append("<p><strong>Doctor’s Notes:</strong></p>");
	        sb.append("<blockquote style='margin:10px 0; padding:10px; background-color:#f9f9f9; border-left:3px solid #4a90e2;'>")
	          .append(escapeHtml(notes))
	          .append("</blockquote>");
	    }
	    
	   

	    sb.append("<p>The prescription document is attached to this email. ")
	      .append("You can also view and download this prescription anytime in the <strong>All-Cures</strong> mobile application. ")
	      .append("If you experience any issues opening the file, please contact our support team for assistance.</p>");

	    sb.append("<p>Thank you for trusting <strong>All-Cures Healthcare Platform</strong>—")
	      .append("your partner in accessible, secure, and reliable healthcare services.</p>");

	    // ✅ Optional Follow-Up Date
//	    if (followUpIso != null && !followUpIso.isBlank()) {
//	        sb.append("<p><strong>Follow-Up Appointment:</strong> ")
//	          .append("Please remember to book a follow-up appointment on <strong>")
//	          .append(escapeHtml(followUpIso))
//	          .append("</strong> for continued care.</p>");
//	    }
	    sb.append("<p style='margin-top:20px;'>Warm regards,<br/>")
	      .append("<strong>The All-Cures Team</strong></p>");
	    sb.append("</div>");

	    return sb.toString();
	}

	private String escapeHtml(String s) {
		if (s == null)
			return null;
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
				"&#39;");
	}

	@PreDestroy
	public void shutdown() {
		try {
			executor.shutdown();
			if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
