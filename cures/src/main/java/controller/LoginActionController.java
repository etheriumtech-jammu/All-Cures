package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.JWTTokenValidationInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import dao.RegistrationDaoImpl_New;
import model.Registration;
import dao.FCMDao;
import util.Constant;
import util.CookieManager;
import util.Encryption;
import util.EnDeCryptor;

/**
 * Servlet implementation class LoginActionController
 */
@WebServlet("/add_cookies")
public class LoginActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SALT = "my-salt-text";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session= request.getSession();
		Encryption encrypt = new Encryption();
		CookieManager cook = new CookieManager();
		String idcook =null;
		// Reading data from the body of a POST request
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        stringBuilder.append(line);
	    }
	    String body = stringBuilder.toString();

		response.setContentType("text/html");
		String email = request.getParameter(Constant.EMAIL);
		String saltedPassword = request.getParameter(Constant.PSW);
		String destinationUrl = request.getParameter("destinationUrl");
		Constant.log("????????????????????????::::::::::::::"+destinationUrl, 0);
		//HttpSession session = request.getSession();
		String value= JWTTokenValidationInterceptor.generateJWTToken(email);
		String hashedPassword = null;
		final String secretKey = Constant.SECRETE;		
		hashedPassword = encrypt.encrypt(saltedPassword, secretKey);		
		PrintWriter out = response.getWriter();

		String remme= (request.getParameter(Constant.REMPWD) == null || "".equals(request.getParameter(Constant.REMPWD))) ? Constant.OFF : (String) request.getParameter(Constant.REMPWD);
		//ToDo: This implementation should not be static as this will cause overwrite issues in a multi user environment
		Registration user = RegistrationDaoImpl_New.findAllUsers(email, hashedPassword);
		if(user != null){

			//Logging Password in Logs only in DEBUG Mode
			Constant.log("Found at least one user with:"+email+" and pass combination"+hashedPassword, 0);
			if (null != user.getLogin_attempt() && user.getLogin_attempt() > Constant.login_attempts_max) {
				Constant.log("Maximum login attemps limit crossed, Please contact ADMIN", 0);
				//response.sendRedirect("Maximum login attemps limit crossed, Please contact ADMIN");
				response.setStatus(401);
				out.write("Maximum login attemps limit crossed, Please contact ADMIN");
				out.flush();
			}
			
			RegistrationDaoImpl_New.resetLoginDetails(user.getRegistration_id());
			//Calling method to send the token to the user
			 // Parse the JSON body and extract the value of the key "FCM"
			 System.out.println("body"+body);
		    if(body != null && !body.isEmpty()) {
		    	 JSONObject jsonObject = new JSONObject(body);
			    System.out.println(jsonObject);
		 	    String fcmValue = jsonObject.getString("FCM");
				   String deviceType=jsonObject.getString("deviceType");
		 	    // Print the value of "FCM"
		 	    System.out.println("FCM value: " + fcmValue);
		 	   FCMDao.Token_Add(fcmValue, user.getRegistration_id(),deviceType);
		    }
		   
			user.setValue(value);
			
			//User should be logged in now
			session.setAttribute(Constant.USER, user);

			if(remme.equalsIgnoreCase(Constant.ON)){
				//Cookie[] cookies = nul
				Constant.log("Remember Me On So Storing Cookies", 0);
				//Poor Coding here storing the email and password in the Cookie itself; The presence of the cookie itself is an indicator 
				//that you should be seamlessly signed in and maybe what we put into the cookie is your id for lookup purposes, that too encrypted
				//TODO: Fix based on above
				//idcook=cook.storeCookiee(email, hashedPassword);
				//FUCP: Creating the cookie and then Decrypting it in the next step; Gosh!! 
				//user=cook.getUserFromPermCookie(idcook);				
				//Why setting the perm cookie in the session object; This should be set in the response 
				//session.setAttribute(Constant.COOKIE,idcook);
				cook.dropAllCookies(response, user);
			}else{
				Constant.log("No Remember Me Flag Selected", 0);
				//Only Dropping Session Cookie; Not the Perm Cookie
				cook.dropSessionCookies(response, user);
				// response.addCookie(pass);
			}
			// TODO Auto-generated method stub
			String cmd = request.getParameter("cmd");
			if(cmd != null && "login".equalsIgnoreCase(cmd)){
				Constant.log("Ajax Request for Login Made", 0);
				try{
					Gson gson = new GsonBuilder().serializeNulls().create();	
					response.setStatus(200);
					String jsondata = gson.toJson(user);
					out.write(jsondata);
					out.flush();
				}catch(Exception e){
					Constant.log("Error while writing response back for AJAX Login Request", 3);
					e.printStackTrace();
				}
			}else{
				if(destinationUrl != null){
					Constant.log("Going to:"+destinationUrl, 0);
					response.sendRedirect(destinationUrl);
				}else{
					Constant.log("Going to default:"+Constant.DEFAULT_POSTLOGIN_PAGE, 0);
					response.sendRedirect(Constant.DEFAULT_POSTLOGIN_PAGE);
				}
			}
		}else{
			RegistrationDaoImpl_New.updateLoginDetails(email);
			//No user found with credentials
			Constant.log("Going to login page with error and destination url", 0);
			//response.sendRedirect("/cures/login1.jsp?errMsg=99&destinationUrl="+destinationUrl);
			Registration user2 =RegistrationDaoImpl_New.findUserByEmail(email);
			Integer additionsMsg = 1;
			if (null != user2) {
				additionsMsg = user2.getLogin_attempt();
			}
			response.setStatus(401);
			//response.sendRedirect("Incorrect email/password! attemps#"+additionsMsg);
			out.write("Incorrect email/password! attemps#"+additionsMsg);
			out.flush();
		}
	}

	/**8
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
