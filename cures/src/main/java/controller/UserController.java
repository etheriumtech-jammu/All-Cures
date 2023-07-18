package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.RegistrationDaoImpl;
import model.EmailDTO;
import service.SendEmailService;
import util.Constant;
import util.Encryption;
import util.Test;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private RegistrationDaoImpl registrationDaoImpl;
	@Autowired
	private SendEmailService emailUtil;

	@RequestMapping(value = "/updatepassword", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody String updatePassword(@RequestBody HashMap reqBody) {
		String saltedPassword = (String) reqBody.get("updated_password");
		String email = (String) reqBody.get("email");
		String hashedPassword = null;
		final String secretKey = Constant.SECRETE;
		Encryption encrypt = new Encryption();

		hashedPassword = encrypt.encrypt(saltedPassword, secretKey);
		Constant.log("????????????????????????::::::::::::::" + hashedPassword, 0);

		return registrationDaoImpl.updatePassword(hashedPassword, email);
	}

	@RequestMapping(value = "/checkemail", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int chekemail(@RequestBody HashMap reqBody) {
		String email = (String) reqBody.get("email");
//		String hashedPassword = null;
//		final String secretKey = Constant.SECRETE;
//		Encryption encrypt = new Encryption();
//
//		hashedPassword = encrypt.encrypt(saltedPassword, secretKey);
		Constant.log("????????????????????????::::::::::::::" + email, 0);

		return registrationDaoImpl.checkEmail(email);
	}

	// @RequestMapping(value = "/getemaildecrypt?e={em}", produces =
	// "application/json", method = RequestMethod.GET)
	@RequestMapping(value = "/getemdecrypt", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody String getEmailDecrypted(@RequestParam String em) {
		String email = null;
		final String secretKey = Constant.SECRETE;
		Encryption encrypt = new Encryption();

		email = encrypt.decrypt(em, secretKey);
		Constant.log("????????????????????????::::::::::::::" + email, 0);

		return email;
	}

	@RequestMapping(value = "/getemdecrypt", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody String getEmailDecryptedPost(@RequestBody HashMap reqBody) {
		String email = (String) reqBody.get("email");
		final String secretKey = Constant.SECRETE;
		Encryption encrypt = new Encryption();

		String em = encrypt.decrypt(email, secretKey);
		Constant.log("????????????????????????::::::::::::::" + em, 0);

		return em;
	}

	@RequestMapping(value = "/getemencrypt", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody String getEmailEncrypted(@RequestParam String em) {
		String emailEnc = null;
		final String secretKey = Constant.SECRETE;
		Encryption encrypt = new Encryption();

		emailEnc = encrypt.encrypt(em, secretKey);
		Constant.log("????????????????????????::::::::::::::" + em, 0);

		return emailEnc;
	}

	@RequestMapping(value = "/subscribe/{mobile}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody String subscribe(@PathVariable long mobile, @RequestBody HashMap ns_map) {

		Constant.log("????????????????????????::::::::::::::" + mobile, 0);

		return registrationDaoImpl.subscribe(mobile, ns_map);
	}

	@RequestMapping(value = "/updatesubscribe/{mobile}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updatesubscribe(@PathVariable long mobile, @RequestBody HashMap ns_map) {

		Constant.log("????????????????????????::::::::::::::" + mobile, 0);

		return registrationDaoImpl.updatesubscribe(mobile, ns_map);
	}

	@RequestMapping(value = "/unsubscribe/{mobile}/cc/{country_code}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int unsubscribe(@PathVariable long mobile,@PathVariable int country_code) {

		Constant.log(" unsubscribe????????????????????????::::::::::::::" + mobile + " and county_code=" + country_code, 0);

		return registrationDaoImpl.unsubscribe(mobile,country_code);
	}

	@RequestMapping(value = "/subscriptiondetails/{mobile}/cc/{country_code}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody ArrayList getSubscriptionDetails(@PathVariable long mobile,@PathVariable int country_code) {

		Constant.log("????????????????????????::::::::::::::" + mobile, 0);

		return registrationDaoImpl.getSubscriptionDetail(mobile,country_code);
	}

	@RequestMapping(value = "/testemail", method = RequestMethod.POST)
	@ResponseBody
	public String sendMail(@RequestBody HashMap messageHtml) throws MessagingException {
		String message = (String) messageHtml.get("message");
		message = "Hi User,\r\n" + message + "\r\n<b>Thanks</>\r\n Anil Raina";
		try {
			EmailDTO email = new EmailDTO();

			email.setTo("anilraina@etheriumtech.com");
			email.setFrom("anilraina@etheriumtech.com");
			email.setSubject("1Welcome Letter via Spring Boot + FreeMarker");

			// Populate the template data
			Map<String, Object> templateData = new HashMap<>();
			templateData.put("templatefile", "welcome.ftlh");
			templateData.put("name", "Arnav Koul1");
			// List of team members...
			List<String> teamMembers = Arrays.asList("Anil1", "Ajay1", "Ayush", "Arnav");
			templateData.put("teamMembers", teamMembers);
			templateData.put("location", "Jammu, India");
			email.setEmailTemplateData(templateData);
			String returnEmail = emailUtil.shootEmail(email);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Email Sent Successfully.!";
	}

}
