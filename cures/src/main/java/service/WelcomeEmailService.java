package service;

import util.Constant;
import util.EnDeCryptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WelcomeEmailService - sends welcome emails with auto-created password.
 *
 * Loads mail.* properties or falls back to legacy cures.properties keys (SMTP_HOST, SMTP_PORT, etc).
 * Decrypts password if encrypted value present. Single-send (no retries). Async helper exists.
 *
 * Improvements:
 *  - Proper handling for implicit SSL (port 465) and STARTTLS (port 587)
 *  - Increased timeouts and optional mail.debug
 *  - Uses explicit Transport.connect/sendMessage for clearer behavior
 */
@Service
public class WelcomeEmailService {

    private static final Logger LOGGER = Logger.getLogger(WelcomeEmailService.class.getName());

    // Primary config - can be provided in application.properties or cures.properties
    @Value("${mail.host:}")
    private String smtpHost;

    @Value("${mail.port:}")
    private String smtpPort;

    // fallback default port if nothing provided
    @Value("${mail.default.port:587}")
    private String defaultPort;

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

    @Value("${mail.debug:false}")
    private boolean mailDebug;

    @Value("${mail.legacy.props:cures.properties}")
    private String legacyPropsName;

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
            // attempt to load legacy props if mail.* not set
            loadLegacyPropertiesIfPresent();

            // if port still blank, use default
            if (smtpPort == null || smtpPort.isBlank()) {
                smtpPort = defaultPort;
            }

            // decrypt password if needed
            if (smtpPasswordEncrypted != null && !smtpPasswordEncrypted.isBlank()) {
                try {
                    EnDeCryptor enc = new EnDeCryptor();
                    smtpPassword = enc.decrypt(smtpPasswordEncrypted, Constant.SECRETE);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to decrypt smtpPasswordEncrypted", e);
                    smtpPassword = smtpPasswordPlain; // fallback
                }
            } else {
                smtpPassword = smtpPasswordPlain;
            }

            LOGGER.info(String.format("WelcomeEmailService initialized (host=%s, port=%s, user=%s, from=%s, pwdPresent=%b, debug=%b)",
                    smtpHost, smtpPort, smtpUsername, smtpFrom, smtpPassword != null && !smtpPassword.isBlank(), mailDebug));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize WelcomeEmailService", e);
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
                smtpFrom = (smtpFrom == null || smtpFrom.isBlank()) ? smtpUsername : smtpFrom;
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
     * Send a welcome email containing the auto-created password.
     * Single attempt only - no retry/backoff.
     *
     * @param userEmail   recipient email (required)
     * @param firstName   recipient first name (optional)
     * @param tempPassword auto-created password (required)
     * @return true if sent successfully, false otherwise
     */
    public boolean sendWelcomeEmail(String userEmail, String firstName, String tempPassword) {
        if (userEmail == null || userEmail.isBlank()) {
            LOGGER.warning("No user email provided; abort welcome email.");
            return false;
        }

        Session session = null;
        try {
            session = createSession();
            if (session == null) {
                LOGGER.severe("Failed to create mail Session (null). Aborting email send.");
                return false;
            }

            // Optionally enable session-level debug
            if (mailDebug) session.setDebug(true);

            MimeMessage message = new MimeMessage(session);

            String fromAddr = (smtpFrom != null && !smtpFrom.isBlank()) ? smtpFrom : smtpUsername;
            message.setFrom(new InternetAddress(fromAddr, "All-Cures Healthcare Team"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Welcome to All-Cures — your account details", "UTF-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(buildHtmlBody(firstName, userEmail, tempPassword), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            // Use explicit Transport connect/send for clearer errors and control
            Transport transport = null;
            try {
                transport = session.getTransport("smtp");

                int port = parsePortOrDefault(smtpPort, Integer.parseInt(defaultPort));
                // connect(auth) will perform TLS/SSL handshake depending on props
                if (smtpUsername != null && !smtpUsername.isBlank() && smtpPassword != null && !smtpPassword.isBlank()) {
                    transport.connect(smtpHost, port, smtpUsername, smtpPassword);
                } else {
                    transport.connect();
                }

                transport.sendMessage(message, message.getAllRecipients());
                LOGGER.info("Welcome email sent to " + userEmail);
                return true;
            } finally {
                if (transport != null) {
                    try { transport.close(); } catch (Exception ignored) {}
                }
            }
        } catch (SendFailedException sfe) {
            LOGGER.log(Level.SEVERE, "SendFailedException sending welcome email (permanent failure)", sfe);
            return false;
        } catch (MessagingException me) {
            LOGGER.log(Level.SEVERE, "MessagingException sending welcome email", me);
            return false;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected exception when sending welcome email", ex);
            return false;
        }
    }

    /**
     * Non-blocking submit; returns a Future. Fire-and-forget when caller doesn't call get().
     */
    public Future<?> sendWelcomeEmailAsync(String userEmail, String firstName, String tempPassword) {
        return executor.submit(() -> sendWelcomeEmail(userEmail, firstName, tempPassword));
    }

    private int parsePortOrDefault(String portStr, int defaultPortInt) {
        try {
            return Integer.parseInt(portStr);
        } catch (Exception e) {
            return defaultPortInt;
        }
    }

    private Session createSession() {
        try {
            Properties props = new Properties();

            // required: host + port
            if (smtpHost != null && !smtpHost.isBlank()) props.put("mail.smtp.host", smtpHost);
            if (smtpPort != null && !smtpPort.isBlank()) props.put("mail.smtp.port", smtpPort);
            else props.put("mail.smtp.port", defaultPort);

            // Decide SSL vs STARTTLS:
            // If explicit flag or port 465 -> implicit SSL
            boolean useImplicitSsl = smtpSslEnable || "465".equals(smtpPort);

            props.put("mail.smtp.ssl.enable", String.valueOf(useImplicitSsl));
            // For implicit SSL, STARTTLS should be false
            props.put("mail.smtp.starttls.enable", String.valueOf(!useImplicitSsl && smtpStartTlsEnable));

            // auth if username provided
            props.put("mail.smtp.auth", smtpUsername != null && !smtpUsername.isBlank() ? "true" : "false");

            // timeouts (ms) - increased to 30s
            props.put("mail.smtp.connectiontimeout", "30000");
            props.put("mail.smtp.timeout", "30000");
            props.put("mail.smtp.writetimeout", "30000");

            // helpful for debugging or certificate trust issues
            if (smtpHost != null && !smtpHost.isBlank()) {
                props.put("mail.smtp.ssl.trust", smtpHost);
            }

            // optional JavaMail debug through properties
            props.put("mail.debug", String.valueOf(mailDebug));

            if (smtpUsername != null && !smtpUsername.isBlank() && smtpPassword != null && !smtpPassword.isBlank()) {
                return Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUsername, smtpPassword);
                    }
                });
            } else {
                return Session.getInstance(props);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to build mail Session properties", ex);
            return null;
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

        sb.append("<p>If you did not request this account, please contact our support team.</p>");

        sb.append("<p style='margin-top:20px;'>Warm regards,<br/><strong> All-Cures Team</strong></p>");
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
