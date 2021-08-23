package controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}