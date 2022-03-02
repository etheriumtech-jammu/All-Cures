package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.RegistrationDaoImpl;
import model.Registration;
import util.Constant;
import util.CookieManager;
import util.EnDeCryptor;

/**
 * Servlet implementation class RegistrationActionController
 * TODO: Exception Handling and Passing Error Msgs to the FE
 */
public class RegistrationActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      
	 *      Registration Requests are always AJAX requests so no need to check for an Ajax call
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		String firstname = request.getParameter(Constant.FIRSTNAME);
		String lastname = request.getParameter(Constant.LASTNAME);
		String email = request.getParameter(Constant.EMAIL);
		String psw = request.getParameter(Constant.PSW);
		String psw_repeat = request.getParameter(Constant.PSWREPEAT);
		String rem_pwd = request.getParameter(Constant.REMPWD) == null ? Constant.OFF : request.getParameter(Constant.REMPWD);
		String docpatient = request.getParameter(Constant.DOCPATIENT);
		String acceptTnC = request.getParameter(Constant.AcceptTermsAndConditions);
		String acceptPolicy = request.getParameter(Constant.AcceptPolicy);
		String number = request.getParameter(Constant.MOBILE_NUMBER);
		Long mobile = Long.parseLong(number);
		Integer doc_patient = null;
		String errMsg = "";	
		if (docpatient != null && docpatient.trim().equalsIgnoreCase(Constant.DOCTOR)) {
			doc_patient = 1;
		}else if (docpatient != null && docpatient.trim().equalsIgnoreCase(Constant.PATIENT)) {
			doc_patient = 2;
		}else{
			//Default to Patient Reg
			doc_patient = 2;
		}
		Integer rem = 0;
		if(!(Constant.OFF).equalsIgnoreCase(rem_pwd.trim())){
			rem = 1;
		}
		Boolean accTerms = false;
		if(acceptTnC != null && acceptTnC.equalsIgnoreCase(Constant.ON)){
			accTerms = true;
		}
		Boolean accPolicy  = false;
		if(acceptPolicy != null && acceptPolicy.equalsIgnoreCase(Constant.ON)){
			accPolicy = true;
		}
		Integer state = 1;
		Registration user = null;
		
		// System.out.println("hjghghgghhhhhhhhh"+rem);
		//TODO: Remove HTML from Code and Send Error Message back to Page
		//TODO: This should be a front end check; The only check that should be made on the backend is if the user already exists
		PrintWriter out = response.getWriter();
		//Make All Validation Checks Here Before Going to the Final Else to process the request
		if (alreadyExists(email)) {
			errMsg = "Email Address already Exists in the System";
		}else{
			Constant.log("Registering New User Into DB with:"+email, 2);
			user = registerUser(firstname, lastname, psw, email, accTerms, doc_patient, accPolicy, state, rem, mobile);
			if(user != null){
				Constant.log("User Registered Successfully:"+email, 1);
				//Now that the doctor/patient is signed up, should we log her in as well?
				//TODO: LogUserIn
				(request.getSession()).setAttribute(Constant.USER, user);
				CookieManager cook = new CookieManager();
				Constant.log("Dropping Cookies Now", 0);
				if(rem_pwd.equalsIgnoreCase(Constant.ON)){
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
			}else{
				Constant.log("Error Registering User with email:"+email, 1);
				errMsg = "Error While Trying to Register User";
			}			
		}
		
		if(request.getParameter("destinationUrl") != null){
			request.getSession().getServletContext().setAttribute("user", user);
			if(!errMsg.equals("")){				
				response.sendRedirect("/cures/registration.jsp?errMsg="+errMsg);
			}else{
				response.sendRedirect(request.getParameter("destinationUrl"));
			}
		}else{
			if(!errMsg.equals("")){
				response.setStatus(200);
				response.setHeader("errMsg", errMsg);
				//out.write("/cures/index.jsp?errMsg="+errMsg);
				out.write(errMsg);
				out.flush();
			}else{
				response.setContentType("application/json");
				Gson gson = new GsonBuilder().serializeNulls().create();	
				String jsondata = gson.toJson(user);
				out.write(jsondata);
				out.flush();
			}			
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public Registration registerUser(String fName,String lName,String pass,String email,Boolean acceptTerms,Integer docOrPat,
			Boolean acceptPolicy,Integer state,Integer remPwd, Long mobile_number){
		Registration user = null;
		try{
			RegistrationDaoImpl regDao = new RegistrationDaoImpl();
			EnDeCryptor encrypt = new EnDeCryptor();
			//Test encrypt = new Test();
			String hashedPass = null;
			final String secretKey = Constant.SECRETE;
			hashedPass = encrypt.encrypt(pass, secretKey);	
			//just to load spring features
			RegistrationDaoImpl myBean = (RegistrationDaoImpl) SpringUtils.ctx.getBean(RegistrationDaoImpl.class);

			user = myBean.saveRegistration(fName, lName, hashedPass, email, acceptTerms, docOrPat, acceptPolicy, state, remPwd,mobile_number);	
		}catch(Exception e){
			Constant.log("Error While Trying to Register User", 3);
			e.printStackTrace();
		}			
		return user;
	}
	
	public boolean alreadyExists(String email){
		Constant.log("Checking if already exists, user with email:"+email, 1);
		CookieManager cMgr = new CookieManager();
		if(cMgr.getUserFromEmailAddress(email) != null){
			Constant.log("Found user with email:"+email, 1);
			return true;
		}
		Constant.log("Did NOT Find user with email:"+email, 1);
		return false;
	}

}
