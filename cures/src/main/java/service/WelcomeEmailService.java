package service;

import util.Constant;
import util.EnDeCryptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WelcomeEmailService - sends welcome emails with auto-created password.
 */
@Service
public class WelcomeEmailService {

    private static final Logger LOGGER = Logger.getLogger(WelcomeEmailService.class.getName());

    // SMTP config - same property names as PrescriptionEmailService for consistency
    @Value("${mail.host:}")
    private String smtpHost;

    @Value("${mail.port:587}")
    private String smtpPort;

    @Value("${mail.username:}")
    private String smtpUsername;

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

    @Value("${mail.legacy.props:cures.properties}")
    private String legacyPropsName;

    @Value("${mail.retry.attempts:3}")
    private int retryAttempts;

    @Value("${mail.retry.backoff.ms:1000}")
    private long retryBackoffMs;

    // internal decrypted password
    private String smtpPassword;

    // Executor for async sending (single threaded, fire-and-forget)
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "WelcomeEmailService-Sender");
        t.setDaemon(true);
        return t;
    });

    @PostConstruct
    public void init() {
        try {
            // Optionally reuse PrescriptionEmailService's legacy loader logic if needed.
            // For simplicity, try to decrypt encrypted password if present, otherwise use plain.
            if (smtpPasswordEncrypted != null && !smtpPasswordEncrypted.isBlank()) {
                EnDeCryptor enc = new EnDeCryptor();
                smtpPassword = enc.decrypt(smtpPasswordEncrypted, Constant.SECRETE);
            } else {
                smtpPassword = smtpPasswordPlain;
            }

            LOGGER.info(String.format("WelcomeEmailService initialized (host=%s, port=%s, from=%s)",
                    smtpHost, smtpPort, smtpFrom));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize WelcomeEmailService", e);
        }
    }

    /**
     * Send a welcome email containing the auto-created password.
     *
     * @param userEmail   recipient email (required)
     * @param firstName   recipient first name (optional)
     * @param tempPassword auto-created password (required)
     * @return true if sent successfully (false on permanent failure or retries exhausted)
     */
    public boolean sendWelcomeEmail(String userEmail, String firstName, String tempPassword) {
        if (userEmail == null || userEmail.isBlank()) {
            LOGGER.warning("No user email provided; abort welcome email.");
            return false;
        }
        int attempt = 0;
        while (attempt < Math.max(1, retryAttempts)) {
            attempt++;
            try {
                Session session = createSession();
                MimeMessage message = new MimeMessage(session);

                String fromAddr = (smtpFrom != null && !smtpFrom.isBlank()) ? smtpFrom : smtpUsername;
                message.setFrom(new InternetAddress(fromAddr, "All-Cures Healthcare Team"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
                message.setSubject("Welcome to All-Cures — your account details", "UTF-8");

                // HTML body with note about changing password
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(buildHtmlBody(firstName, userEmail, tempPassword), "text/html; charset=utf-8");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(htmlPart);

                message.setContent(multipart);

                Transport.send(message);
                LOGGER.info("Welcome email sent to " + userEmail);
                return true;
            } catch (SendFailedException sfe) {
                LOGGER.log(Level.SEVERE, "SendFailedException sending welcome email (will not retry): attempt " + attempt, sfe);
                return false;
            } catch (MessagingException me) {
                LOGGER.log(Level.WARNING, "MessagingException on attempt " + attempt + " of " + retryAttempts, me);
                if (attempt >= retryAttempts) {
                    LOGGER.log(Level.SEVERE, "Exceeded retry attempts sending welcome email", me);
                    return false;
                } else {
                    try {
                        Thread.sleep(retryBackoffMs * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unexpected exception when sending welcome email", ex);
                return false;
            }
        }
        return false;
    }

    /**
     * Non-blocking submit; returns a Future. Fire-and-forget when caller doesn't call get().
     */
    public Future<?> sendWelcomeEmailAsync(String userEmail, String firstName, String tempPassword) {
        return executor.submit(() -> sendWelcomeEmail(userEmail, firstName, tempPassword));
    }

    private Session createSession() {
        Properties props = new Properties();
        if (smtpHost != null && !smtpHost.isBlank()) props.put("mail.smtp.host", smtpHost);
        if (smtpPort != null && !smtpPort.isBlank()) props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", smtpUsername != null && !smtpUsername.isBlank() ? "true" : "false");
        props.put("mail.smtp.starttls.enable", String.valueOf(smtpStartTlsEnable));
        props.put("mail.smtp.ssl.enable", String.valueOf(smtpSslEnable));

        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

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

    private String buildHtmlBody(String firstName, String userEmail, String tempPassword) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family: Arial, sans-serif; font-size:14px; color:#333;'>");
        sb.append("<p>Dear ").append(escapeHtml(firstName != null ? firstName : "User")).append(",</p>");
        sb.append("<p>Welcome to <strong>All-Cures Healthcare Platform</strong>! Your account has been created successfully.</p>");

        sb.append("<p><strong>Login details</strong></p>");
        sb.append("<table style='border-collapse:collapse; font-size:14px;'>");
        sb.append("<tr><td style='padding:4px 8px; font-weight:600;'>Email:</td><td style='padding:4px 8px;'>")
          .append(escapeHtml(userEmail)).append("</td></tr>");
        sb.append("<tr><td style='padding:4px 8px; font-weight:600;'>Password:</td><td style='padding:4px 8px;'>")
          .append(escapeHtml(tempPassword)).append("</td></tr>");
        sb.append("</table>");

        sb.append("<p>Please change your password after your first login. For your security, avoid reusing passwords used on other sites.</p>");

        sb.append("<p>If you did not request this account, please contact our support team immediately.</p>");

        sb.append("<p style='margin-top:20px;'>Warm regards,<br/><strong>The All-Cures Team</strong></p>");
        sb.append("</div>");
        return sb.toString();
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
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
