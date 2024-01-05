package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.CityDaoImpl;
import dao.RegistrationDaoImpl;
import dao.RegistrationDaoImpl_New;
import dao.VideoDaoImpl;
import model.Registration;
import util.Constant;
import util.CookieManager;
import util.EnDeCryptor;

@RestController
@RequestMapping(path = "/registration")
public class RegistrationController {

	@RequestMapping(value = "/add/new", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody String RegisterUser(@RequestBody HashMap RegisterMap,HttpServletRequest request,HttpServletResponse response ) throws Exception {

	return	RegistrationDaoImpl_New.RegisterUser(RegisterMap,request,response);
	  
	}
	
}
	
