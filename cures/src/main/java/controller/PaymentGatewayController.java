package controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.CityDaoImpl;
import model.Registration;
import service.TokenValidationInterceptor;

@RestController
@RequestMapping(path = "/make")
public class PaymentGatewayController {

	@Autowired
	private CityDaoImpl cityDaoImpl;

	@RequestMapping(value = "/Payment", produces = "application/json", method = RequestMethod.GET)
	
	public @ResponseBody String getCityDetails() {
		
	//	return cityDaoImpl.getAllCityDetails();
		return "Hello";
	}
}
