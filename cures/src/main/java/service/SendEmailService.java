package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import controller.SpringUtils;
import model.EmailDTO;
import model.Registration;
import util.Constant;
import util.EnDeCryptor;

@Service
@Configurable
public class SendEmailService {

//	@Autowired
//	private JavaMailSender emailSender;

//	@Autowired
//	private SpringTemplateEngine templateEngine;

//	@Autowired
//	private static Configuration freemarkerConfig;
//	@Autowired
//	private static FreeMarkerConfigurer freeMarkerConfigurer;

	public void sendEmail(String to, String subject, String messageText) throws MessagingException, IOException {

//        Mail mail = new Mail();
//
//		Map<String, Object> model = new HashMap<String, Object>();
//        model.put("name", "Developer!");
//        model.put("location", "United States");
//        model.put("sign", "Java Developer");
//        mail.setProps(model);

//		MimeMessage message = emailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//				StandardCharsets.UTF_8.name());
//		helper.addAttachment("template-cover.png", new ClassPathResource("javabydeveloper-email.PNG"));
//		Context context = new Context();
//		// context.setVariables(mail.getProps());
//
//		String html = templateEngine.process("newsletter-template", context);
//		helper.setTo(to);
//		helper.setText(html, true);
//		helper.setSubject(subject);
//		helper.setFrom("info@etheriumtech.com");
//		emailSender.send(message);
	}

//	@Autowired
//	static    HttpServletRequest request; 
	// public String shootEmail(String to, String subject, String
	// mailContentHtml,Map<String, Object> templateData) throws IOException {
	public String shootEmail(EmailDTO email) throws IOException {

		// Recipient's email ID needs to be mentioned.
		String to = email.getTo();
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
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
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
			if (null != email.getFrom())
				message.setFrom(email.getFrom());
			else
				message.setFrom(new InternetAddress(prop.getProperty("SMTP_EMAIL_FROM_INFO")));
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// Set Subject: header field
			message.setSubject(email.getSubject());
			// Now set the actual message
			// message.setText(messageText);
			System.out.println("###### Email sending initiated ######");

			String mailContentHtml2;
			if (null != email.getEmailtext())
				mailContentHtml2 = email.getEmailtext();
			else
				mailContentHtml2 = geFreeMarkerTemplateContent(email.getEmailTemplateData());

			message.setContent(mailContentHtml2, "text/html;charset=utf-8");

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

	private String geFreeMarkerTemplateContent(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			FreeMarkerConfigurer myBean = (FreeMarkerConfigurer) SpringUtils.ctx.getBean(FreeMarkerConfigurer.class);
			String templateFile = (String) model.get("templatefile");
			content.append(FreeMarkerTemplateUtils
					.processTemplateIntoString(myBean.getConfiguration().getTemplate(templateFile), model));
			// .processTemplateIntoString(myBean.getConfiguration().getTemplate("welcome.ftlh"),
			// model));
			return content.toString();
		} catch (Exception e) {
			System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());

		}
		return "ERROR!";
	}

}