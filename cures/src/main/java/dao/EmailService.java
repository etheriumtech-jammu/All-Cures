package dao;

import org.springframework.stereotype.Service;

import model.EmailDTO;

//@Component
@Service
public class EmailService {

	/*
	 * @Autowired private JavaMailSender mailSender;
	 * 
	 * @Autowired private FreeMarkerConfigurer freemarkerConfig;
	 */

	public String sendWelcomeEmail(EmailDTO emailDTO) {
		System.out.println("##### Started sending welcome email ####");
		String templateContent = "";
		//MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {

//			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
//					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//					StandardCharsets.UTF_8.name());

//			templateContent = FreeMarkerTemplateUtils
//					.processTemplateIntoString(freemarkerConfig.getConfiguration()
//							.getTemplate("welcome.ftlh"),
//							emailDTO.getEmailData());

//			helper.setTo(emailDTO.getTo());
//			helper.setFrom(emailDTO.getFrom());
//			helper.setSubject(emailDTO.getSubject());
//			helper.setText(templateContent, true);
//			mailSender.send(mimeMessage);

			System.out.println("######## Welcome email sent ######");
		} catch (Exception e) {
			System.out.println("Sending welcome email failed, check log...");
			e.printStackTrace();
		}
		return templateContent;
	}
}
