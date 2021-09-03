package util;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpAsyncRequestControl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import model.Registration;

public class SendEmailUtil {
	
//	@Autowired
//	static    HttpServletRequest request; 
	public static String shootEmail(String to, String subject, String messageText) {

		// Recipient's email ID needs to be mentioned.
		// String to = "anilraina@etheriumtech.com";
		if (null == to) {
		    //HttpServletRequest req = (HttpServletRequest) request;
			HttpServletRequest request = 
					((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
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

		// Sender's email ID needs to be mentioned
		String from = "anilraina@etheriumtech.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtpout.secureserver.net";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// properties.put("mail.smtp.starttls.enable", "true");

		// Session session = Session.getInstance(properties);

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("anilraina@etheriumtech.com", "password123");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(messageText);

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "Email sent";

	}

}