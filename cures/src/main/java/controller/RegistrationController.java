package controller;

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
    
}
	
