package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import model.Registration;

public class SendEmailUtil {

//	@Autowired
//	static    HttpServletRequest request; 
	public static String shootEmail(String to, String subject, String messageText) throws IOException {

		// Recipient's email ID needs to be mentioned.
		// String to = "anilraina@etheriumtech.com";
		if (null == to) {
			// HttpServletRequest req = (HttpServletRequest) request;
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			HttpSession session = request.getSession(true);

			int reg_id = 0;
			if (session.getAttribute(Constant.USER) != null) {
				Constant.log("Email UTIL#########USER IS IN SESSION########", 0);
				Registration user = (Registration) session.getAttribute(Constant.USER);
				reg_id = user.getRegistration_id();
				System.out.println(reg_id);
				to = user.getEmail_address();
			}
		}

		// FileReader reader = new FileReader("cures.properties");
		String propFileName = "cures.properties";
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(propFileName);
		final Properties prop = new Properties();

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", prop.getProperty("SMTP_HOST"));
		properties.put("mail.smtp.port", prop.getProperty("SMTP_PORT"));
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// properties.put("mail.smtp.starttls.enable", "true");

		// Session session = Session.getInstance(properties);
		EnDeCryptor aesEncryptionDecryption = new EnDeCryptor();
		final String secretKey = Constant.SECRETE;
		final String decryptedString = aesEncryptionDecryption.decrypt(prop.getProperty("SMTP_EMAIL_PASS_ENC"),
				secretKey);

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(prop.getProperty("SMTP_EMAIL_FROM_INFO"), decryptedString);
			}
		});

		// Used to debug SMTP issues
		session.setDebug(false);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(prop.getProperty("SMTP_EMAIL_FROM_INFO")));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// Set Subject: header field
			message.setSubject(subject);
			// Now set the actual message
			message.setText(messageText);
			System.out.println("sending Email...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return "Error in email sending.";

		}

		return "Email sent successfully";

	}

}