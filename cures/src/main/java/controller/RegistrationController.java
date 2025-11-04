package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import dao.RegistrationDaoImpl_New;
@RestController
@RequestMapping(path = "/registration")
public class RegistrationController {

	//To register a new User
	@RequestMapping(value = "/add/new", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody void RegisterUser(@RequestBody HashMap RegisterMap,HttpServletRequest request,HttpServletResponse response ) throws Exception {
	String res=	RegistrationDaoImpl_New.RegisterUser(RegisterMap,request,response);
	PrintWriter out = response.getWriter();
   	 out.write(res);
   	 out.flush();
	}

	@RequestMapping(value = "/add/auto", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody void registerUserAuto(@RequestBody HashMap<String, Object> registerMap,
	                                           HttpServletRequest request,
	                                           HttpServletResponse response) {
	    try {
	        String daoResponse = RegistrationDaoImpl_New.RegisterUserAuto(registerMap, request, response);

	        // Decide HTTP status based on DAO response content
	        if (daoResponse != null && daoResponse.contains("\"success\":true")) {
	            response.setStatus(HttpServletResponse.SC_OK); // 200
	        } else if (daoResponse != null && daoResponse.toLowerCase().contains("already exists")) {
	            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
	        } else if (daoResponse != null && daoResponse.toLowerCase().contains("internal server")) {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
	        } else {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	        }

	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.write(daoResponse == null ? "{\"success\":false, \"message\":\"Internal server error\"}" : daoResponse);
	        out.flush();

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            response.setContentType("application/json");
	            PrintWriter out = response.getWriter();
	            out.write("{\"success\":false, \"message\":\"Internal server error\"}");
	            out.flush();
	        } catch (IOException ioEx) {
	            ioEx.printStackTrace();
	        }
	    }
	}

}
	
