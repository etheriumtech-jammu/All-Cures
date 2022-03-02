package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.RegistrationDaoImpl;
import model.Registration;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

	@Autowired
	private RegistrationDaoImpl registrationDaoImpl;

	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Registration getProfile(@PathVariable Integer id) {
		return registrationDaoImpl.findUserByRegId(id);
	}

	
	
	
}

