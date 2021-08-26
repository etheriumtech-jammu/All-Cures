package controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.RegistrationDaoImpl;
import util.Constant;
import util.Encryption;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private RegistrationDaoImpl registrationDaoImpl;

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

	//@RequestMapping(value = "/getemaildecrypt?e={em}", produces = "application/json", method = RequestMethod.GET)
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

}