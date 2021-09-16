package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.PatientDaoImpl;
import model.Patient;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

	@Autowired
	private PatientDaoImpl patientDaoImpl;

	@RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Patient getPatientProfile(@PathVariable Integer id) {
		return patientDaoImpl.findAllPatientByPatientid(id);
	}

}