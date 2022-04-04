package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.CityDaoImpl;
import model.Registration;

@RestController
@RequestMapping(path = "/city")
public class CityController {

	@Autowired
	private CityDaoImpl cityDaoImpl;

	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getCityDetails() {
		return cityDaoImpl.getAllCityDetails();

	}
	
	@RequestMapping(value = "/newsletter/{mobile}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Integer getNewsletterDetails(@PathVariable String mobile) {
		return cityDaoImpl.getNewsletterDetails(mobile);     
	}	
	
	@RequestMapping(value = "/state", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getStateDetails() {
		return cityDaoImpl.getAllStateDetails();

	}
	
	@RequestMapping(value = "/disease", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getDiseaseDetails() {
		return cityDaoImpl.getAllDiseaseDetails();

	}
	@RequestMapping(value = "/mobile", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getmobileDetails() {
		return cityDaoImpl.getAllMobileDetails();

	}

}