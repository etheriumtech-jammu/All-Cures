package dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controller.UserController;
import model.AvailabilitySchedule;
import model.EmailDTO;
import model.Registration;
import service.SendEmailService;
import util.Constant;
import util.CookieManager;
import util.EnDeCryptor;
import util.HibernateUtil;
import util.WhatsAPITrackUsersSync;
import util.WhatsAPITrackUsers;

import dao.DeleteDaoImpl;

@Component
public class RegistrationDaoImpl_New {

	@Autowired
	private static SendEmailService emailUtil;

//	@Transactional
	public static Registration saveRegistration(String f_name, String l_name, String pwd, String email, Boolean accept,
	        Integer type, Boolean policy, Integer state, Integer rem, Long mobile,Integer Age) {
	    int docid=0;
	    Registration reg = new Registration();
	    Session session = null;
	    try {
	        session = HibernateUtil.buildSessionFactory(); // Retrieve the current session

	        Constant.log("Registering User with Firstname to DB:" + f_name, 0);
	        
	        if (type == 1) {
	        docid=   DoctorsDaoImpl_New.saveDoctors( f_name, l_name, email);
	        System.out.println("docid" +docid);
	        if(docid!=0) {  
	        
	        session.beginTransaction();
	        reg.setEmail_address(email);
	        reg.setPass_word(pwd);
	        reg.setFirst_name(f_name);
	        reg.setLast_name(l_name);
	        reg.setAcceptance_condition(accept);
	        reg.setRegistration_type(type);
	        reg.setprivacy_policy(policy);
	        reg.setAccount_state(state);
	        reg.setRemember_me(rem);
	        reg.setMobile_number(mobile);
	        reg.setDocID(docid);
	        reg.setAge(Age);
	        session.save(reg);
	        session.getTransaction().commit();
	       
	        }else {
	        	System.out.println("Error in Creating doctor");
	        }
	//        sendRegistrationEmail(session, email, f_name, user);

	//       session.getTransaction().commit();
	    }else{
	    	 session.beginTransaction();
		        reg.setEmail_address(email);
		        reg.setPass_word(pwd);
		        reg.setFirst_name(f_name);
		        reg.setLast_name(l_name);
		        reg.setAcceptance_condition(accept);
		        reg.setRegistration_type(type);
		        reg.setprivacy_policy(policy);
		        reg.setAccount_state(state);
		        reg.setRemember_me(rem);
		        reg.setMobile_number(mobile);
		        reg.setDocID(docid);
		        session.save(reg);
		        session.getTransaction().commit();
	    	
	        	}} catch (Exception e) {
	        		handleException(session, e);
	  		  } finally {
	        
	  		      }
		    return reg;
		}

	@Transactional
	private static int registerDoctor( Session session,String email, String f_name, String l_name, Registration user) {
	    Constant.log("Registering User is a Doctor", 0);
//	 Session   session = HibernateUtil.buildSessionFactory();
	    Registration user1 = RegistrationDaoImpl_New.findUserByEmail(email);
	    System.out.println("user" + user1);
//	    session.beginTransaction();
	    DoctorsDaoImpl_New.saveDoctors( f_name, l_name, email);

	    int docid = DoctorsDaoImpl_New.findDoctorsByEmail(email).getDocID();
	    System.out.println("docid" + docid);
//	    session.beginTransaction();
	    Query query = session
				.createNativeQuery("UPDATE  registration " + " SET DocID =" + docid + " WHERE registration_id = " + user1.getRegistration_id() + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			// session.getTransaction().commit();
			System.out.println("updated registration table ");
//			session.getTransaction().commit();
			session.getTransaction().commit();   //session.close();
			
		} catch (Exception e) {
			Constant.log(e.getStackTrace().toString(), 3);
			session.getTransaction().commit(); //session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}
		return ret;
	     // Return the user object
	}

	
	private static int registerPatient(Session session, String email, String f_name, String l_name, Registration user) {
	    Constant.log("Registering User is a Patient", 0);

//	    Registration user = RegistrationDaoImpl_New.findUserByEmail(email);
	    System.out.println("user" + user);

//	    PatientDaoImpl.savePatient(user.getRegistration_id(), f_name, l_name, email);

	    System.out.println("Added Patient");

	    return 1; // Return the user object
	}

	@Transactional
	private static void sendRegistrationEmail(Session session, String email, String f_name, Registration user) throws IOException {
	    EmailDTO emaildto = new EmailDTO();
	    emaildto.setTo(email);
	    emaildto.setSubject("Registration User: All-Cures");

	    Map<String, Object> templateData = new HashMap<>();
	    templateData.put("templatefile", "email/registration.ftlh");
	    templateData.put("first_name", f_name);
	    String link = "https://all-cures.com";
	    templateData.put("linkverfiy", link);

	    ObjectMapper oMapper = new ObjectMapper();
	    Map<String, Object> mapUser = oMapper.convertValue(user, Map.class);
	    templateData.putAll(mapUser);

	    emaildto.setEmailTemplateData(templateData);
	    System.out.println(emaildto);

	    String returnEmail = emailUtil.shootEmail(emaildto);
	}

	private static void handleException(Session session, Exception e) {
	    if (session.getTransaction() != null && session.getTransaction().isActive()) {
	        session.getTransaction().rollback();
	    }
	    e.printStackTrace();
	}


//Used for Login Lookup
	public static Registration findAllUsers(String email, String pwd) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// Only Logging Password in Logs in Debug Mode
		Constant.log("Finding users with email:" + email + " and pass: " + pwd, 0);
	
			
		// creating transaction object
//		session.beginTransaction();
		Registration register = null;
		Query query = session
				.createNativeQuery("select registration_id, first_name, last_name, email_address, pass_word, "
						+ "registration_type, acceptance_condition, privacy_policy, account_state, remember_me, login_attempt,last_login_datatime"
						+ " ,DocID,Age from registration "
						+ "where email_address='" + email + "' and pass_word='" + pwd + "'");

		ArrayList<Registration> regList = (ArrayList<Registration>) query.getResultList();
		Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:" + email, 1);

		Iterator itr = regList.iterator();
		if (itr.hasNext()) {
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:" + email, 1);
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer) obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String) obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String) obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String) obj[3] : "");
				// Security Best Practice: Do not put password in the user obj in session
				// String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				// register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer) obj[5] : 1);
				register.setAcceptance_condition(obj[6] != null ? (Boolean) obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean) obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer) obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer) obj[9] : 0);
				register.setLogin_attempt(obj[10] != null ? (Integer) obj[10] : 0);
				register.setLast_login_datatime((java.util.Date) obj[11]);
				register.setDocID(obj[12] != null ? (Integer) obj[12] : 0);
				register.setAge(obj[13] != null ? (Integer) obj[13] : 0);
				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}

//		session.getTransaction().commit();   //session.close();
		return register;
	}

	public static Registration findUserByEmail(String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>" + email), 0);
		int docid = 0;

		Registration register = null;
		Query query = session
				.createNativeQuery("select * from registration where email_address='" + email.trim() + "'and (deactivated IS NULL OR deactivated = 0)");
		ArrayList<Registration> list = (ArrayList<Registration>) query.getResultList();
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:" + email, 1);
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer) obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String) obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String) obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String) obj[3] : "");
				// Security Best Practice: Do not put password in the user obj in session
				// String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				// register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer) obj[5] : 1);
				register.setAcceptance_condition(obj[6] != null ? (Boolean) obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean) obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer) obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer) obj[9] : 0);
				register.setLast_login_datatime((Timestamp) obj[10]);
				register.setLogin_attempt(obj[11] != null ? (Integer) obj[11] : 0);
				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}
//		session.getTransaction().commit();   //session.close();
		return register;
	}

	public static Registration findUserByRegId(int regid) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>FINDING USER FOR ID:" + regid), 0);
		int docid = 0;

		Registration register = null;
		Query query = session.createNativeQuery(
				"select registration_id, first_name, last_name, email_address, pass_word, registration_type,"
						+ " acceptance_condition, privacy_policy, account_state, remember_me, last_login_datetime, login_attempt, last_login_datatime,"
						+ " concat(\"\",mobile_number), DocID,Age from registration where registration_id=" + regid);
		ArrayList<Registration> list = (ArrayList<Registration>) query.getResultList();
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for ID:" + regid, 1);
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer) obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String) obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String) obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String) obj[3] : "");
				// Security Best Practice: Do not put password in the user obj in session
				// String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				// register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer) obj[5] : 1);
				register.setAcceptance_condition(obj[6] != null ? (Boolean) obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean) obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer) obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer) obj[9] : 0);
				register.setMobile_number(obj[13] != null ? Long.parseLong((String) obj[13]) : 0);
				register.setDocID(obj[14] != null ? (Integer) obj[14] : 0);
				System.out.println((Integer) obj[14]);
				
				register.setAge(obj[15] != null ? (Integer) obj[15] : 0);

				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}
//		session.getTransaction().commit();   //session.close();
		return register;
	}

	public String updatePassword(String password, String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();
		// Query queryApproved = session.createNativeQuery("UPDATE registration SET
		// pass_word= '"+ password+"' where registration_id = "+reg_id+" );");

		int ret = 0;
		try {
			Query checkEmailExists = session.createNativeQuery(
					"select email_address from  registration where email_address = '" + email + "' ;");

			List<Object[]> results = (List<Object[]>) checkEmailExists.getResultList();
			System.out.println("result list Email Check@@@@@@@@@@@@@ size=" + results.size());
			if (results.size() == 0) {
				return "Sorry, the email address you entered does not exist in our database.";
			}

			System.out.println("check email exists in  registration table for email passed from UI =  " + email);

			Query queryApproved = session.createNativeQuery(
					"UPDATE registration SET pass_word= '" + password + "' where email_address = '" + email + "' ;");

			ret = queryApproved.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated registration table password for email =  " + email);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret + "";
	}

	public static int checkEmail(String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// creating transaction object
//		session.beginTransaction();
		// Query queryApproved = session.createNativeQuery("UPDATE registration SET
		// pass_word= '"+ password+"' where registration_id = "+reg_id+" );");

		int ret = 0;
		try {
			Query checkEmailExists = session.createNativeQuery(
					"select email_address from  registration where email_address = '" + email + "' ;");
			
			List<Object[]> results = (List<Object[]>) checkEmailExists.getResultList();
			System.out.println("result list Email Check@@@@@@@@@@@@@ size=" + results.size());
			if (results.size() == 0) {
				return 0;
			} else {
				
				String encEmail = new UserController().getEmailEncrypted(email);
				// String link = "http://localhost:3000/loginForm/ResetPass/?em=" + encEmail;
				String link = "https://all-cures.com/loginForm/ResetPass/?em=" + encEmail;
				// new SendEmailUtil().shootEmail(email, "Test subject", "Password reset link
				// here...\n" + link);
//				EmailDTO emaildto = new EmailDTO();
//
//				emaildto.setTo(email);
//				emaildto.setSubject("Forgot password..");
//				emaildto.setEmailtext("Dear User \n Password reset link here...\n" + link);
//
//				String returnEmail = emailUtil.shootEmail(emaildto);

				// second email also using template
				EmailDTO emaildto2 = new EmailDTO();

				emaildto2.setTo(email);
				emaildto2.setFrom("All-Cures INFO");
				emaildto2.setSubject("Forgot password: All-Cures");
			
				// Populate the template data
				Map<String, Object> templateData = new HashMap<>();
				templateData.put("templatefile", "email/forgotpassword.ftlh");
				templateData.put("name", email);
				templateData.put("linkforgotpassword", link);
				emaildto2.setEmailTemplateData(templateData);
				
				String returnEmail = emailUtil.shootEmail(emaildto2);
				System.out.println("Hellooo");
				System.out.println("Email sent");
//				session.getTransaction().commit();
				return 1;
			}
//			System.out.println("check email exists in  registration table for email passed from UI =  " + email);
		} catch (Exception ex) {
//			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public static int updateLoginDetails(String email) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();

		java.sql.Timestamp last_login_datetime = new java.sql.Timestamp(new java.util.Date().getTime());
		Query query = session.createNativeQuery(
				"update registration  as dest , (\r\n" + " SELECT (case when login_attempt IS NULL then 1\r\n"
						+ "             else login_attempt + 1\r\n" + "        end) as login_attempt\r\n"
						+ " FROM registration\r\n" + " WHERE email_address = '" + email + "'\r\n" + " ) as src\r\n"
						+ " set dest.login_attempt = src.login_attempt\r\n" + " , dest.last_login_datetime = '"
						+ last_login_datetime + "'\r\n" + " where dest.email_address ='" + email + "';");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated registration table for email_address =  " + email);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit(); //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit(); //session.close();

		return ret;
	}

	public static int resetLoginDetails(int regId) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();

		java.sql.Timestamp last_login_datetime = new java.sql.Timestamp(new java.util.Date().getTime());
		Query query = session
				.createNativeQuery("update registration set login_attempt = 0 \r\n" + " ,deactivated=0,deactivated_time=NULL, reason_id=NULL, last_login_datetime = '"
+ last_login_datetime + "'\r\n" + " where registration_id =" + regId + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("reset registration table for reg_id =  " + regId);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit(); //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit(); //session.close();

		return ret;
	}

	public static String subscribe(long mobile, HashMap ns_map) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

	int result=	checkSubscription(mobile,(String)ns_map.get("country_code"));
		
	if(result == 1 )
	{
		return("Already subscribed");
	}
	
	else {
	// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();
		
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		String nl_start_date = sqlDate.toString();

		String nl_subscription_disease_id = (String) ns_map.get("nl_subscription_disease_id");
		int nl_sub_type = (int) ns_map.get("nl_sub_type");
		String nl_subscription_cures_id = (String) ns_map.get("nl_subscription_cures_id");
		String country_code = (String) ns_map.get("country_code");
		System.out.println("Subscribe create_date>>>>>" + nl_start_date);
		// set active =1 for new subscription
		Query query = session.createNativeQuery("INSERT INTO `newsletter` (\r\n" + "\r\n"
				+ "`nl_subscription_disease_id`,\r\n" + "`nl_start_date`,\r\n" + "`nl_sub_type`,\r\n" + "`mobile`,\r\n"
				+ "`nl_subscription_cures_id`, `active`, `country_code`)\r\n" + " VALUES \r\n" + "('" + "\r\n"
				+ nl_subscription_disease_id + "',\r\n '" + nl_start_date + "' ,\r\n" + nl_sub_type + ",\r\n" + mobile
				+ ",\r\n'" + nl_subscription_cures_id + "',1, " + country_code + ");\r\n");
		// needs other condition too but unable to find correct column
		System.out.println(query);
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to newsletter table for mobile =  " + mobile);
			try {
				String[] params = new String[5];
				params[0] = "+" + country_code;
				params[1] = mobile + "";
				params[2] = nl_sub_type + "";
				params[3] = nl_subscription_disease_id + "";
				params[4] = nl_subscription_cures_id + "";
				WhatsAPITrackUsers.POSTRequestTrackUsers(params);
				System.out.println("Subscription WhatsApp Message sent.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit(); //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit(); //session.close();

		return "Subscribed";
	}
	}

	public static int updatesubscribe(long mobile, HashMap ns_map) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();
		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.currentRequestAttributes()) .getRequest(); HttpSession
		 * sessionreq = request.getSession(true);
		 * 
		 * int reg_id = 0; if (sessionreq.getAttribute(Constant.USER) != null) {
		 * Constant.log("update subscription #########USER IS IN SESSION########", 0);
		 * Registration user = (Registration) sessionreq.getAttribute(Constant.USER);
		 * reg_id = user.getRegistration_id(); System.out.println(reg_id); // to =
		 * user.getEmail_address(); } System.out.println(reg_id);
		 */

		String nl_subscription_disease_id = (String) ns_map.get("nl_subscription_disease_id");
		int nl_sub_type = (int) ns_map.get("nl_sub_type");
		String nl_subscription_cures_id = (String) ns_map.get("nl_subscription_cures_id");
		String country_code = (String) ns_map.get("country_code");

		String updateStr = "";
//		if ((mobile + "").equals("")) {
//			updateStr += " mobile=" + mobile + ",";
//		}
		if (!(nl_subscription_disease_id + "").equals("")) {
			updateStr += " nl_subscription_disease_id='" + nl_subscription_disease_id + "',";
		}
		if (!(nl_sub_type + "").equals("")) {
			updateStr += " nl_sub_type=" + nl_sub_type + ",";
		}
		if (!(nl_subscription_cures_id + "").equals("")) {
			updateStr += " nl_subscription_cures_id='" + nl_subscription_cures_id + "',";
		}
		if (!(country_code + "").equals("")) {
			updateStr += " country_code=" + country_code + ",";
		}

		updateStr = updateStr.replaceAll(",$", "");

		Query queryArticlePromoPaid = session.createNativeQuery("UPDATE newsletter SET " + updateStr
				+ "  WHERE mobile = " + mobile + " and country_code = " + country_code + ";");

		int ret = 0;
		try {
			ret = queryArticlePromoPaid.executeUpdate();
			session.getTransaction().commit();
			System.out.println(
					"updated newsletter table for mobile  =  " + mobile + " and country_code = " + country_code);
//			SendEmailUtil.shootEmail(null, "updated subscription ",
//					"Hi, \n\r updated newsletter table for mobile  =  " + mobile);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public int unsubscribe(long mobile, int country_code) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//				.getRequest();
//		HttpSession sessionreq = request.getSession(true);
//
//		int reg_id = 0;
//		if (sessionreq.getAttribute(Constant.USER) != null) {
//			Constant.log("update subscription #########USER IS IN SESSION########", 0);
//			Registration user = (Registration) sessionreq.getAttribute(Constant.USER);
//			reg_id = user.getRegistration_id();
//			System.out.println(reg_id);
//			// to = user.getEmail_address();
//		}
//		System.out.println(reg_id);

		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		String nl_end_date = sqlDate.toString();

		Query queryArticlePromoPaid = session.createNativeQuery("UPDATE newsletter SET active=0  and nl_end_date = '"
				+ nl_end_date + "' WHERE mobile=" + mobile + " and country_code=" + country_code + ");");

		int ret = 0;
		try {
			ret = queryArticlePromoPaid.executeUpdate();
			session.getTransaction().commit();
			System.out.println("unscribe newsletter table for mobile = " + mobile + " country_code=" + country_code);
//			SendEmailUtil.shootEmail(null, "Unscribed allcures ",
//					"Hi, \n\r updated newsletter table for reg_id  =  " + reg_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public static ArrayList getSubscriptionDetail(long mobile, int country_code) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// Only Logging Password in Logs in Debug Mode
		Constant.log("Finding users with mobile:" + mobile, 0);

		// creating transaction object
//		session.beginTransaction();
		Registration register = null;
		Query query = session.createNativeQuery("SELECT `newsletter`.`user_id`,\r\n"
				+ "    `newsletter`.`nl_subscription_disease_id`," + "    `newsletter`.`nl_start_date`,\r\n"
				+ "    `newsletter`.`nl_sub_type`,\r\n" + "    `newsletter`.`mobile`,\r\n"
				+ "    `newsletter`.`nl_subscription_cures_id`,\r\n" + "    `newsletter`.`active`,\r\n"
				+ "    `newsletter`.`nl_end_date`,\r\n" + "    `newsletter`.`country_code`, "
				+ "(select dc_name from disease_condition dc where dc.dc_id in ( `newsletter`.`nl_subscription_disease_id` )) as disease_name"
				+ " FROM `newsletter`\r\n" + " where mobile=" + mobile + " and country_code=" + country_code + ";");

		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Promo@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int user_id = objects[0] != null ? (int) objects[0] : 0;
			String nl_subscription_disease_id = (String) objects[1];
			java.sql.Date nl_start_date = (java.sql.Date) objects[2];
			int nl_sub_type = objects[3] != null ? (int) objects[3] : 0;
			Double mobile1 = (Double) objects[4];
			String nl_subscription_cures_id = (String) objects[5];
			Integer active = objects[6] != null ? (int) objects[6] : 0;
			java.sql.Date nl_end_date = (java.sql.Date) objects[7];
			int country_code1 = (int) objects[8];
			String disease_name = (String) objects[9];

			hm.put("user_id", user_id);
			hm.put("nl_subscription_disease_id", nl_subscription_disease_id);
			hm.put("nl_start_date", nl_start_date);
			hm.put("nl_sub_type", nl_sub_type);
			hm.put("mobile", mobile1);
			hm.put("nl_subscription_cures_id", nl_subscription_cures_id);
			hm.put("active", active);
			hm.put("nl_end_date", nl_end_date);
			hm.put("country_code", country_code1);
			hm.put("disease_name", disease_name);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return (ArrayList) hmFinal;
	}

	public static int checkSubscription(long mobile, String country_code) {
		Session session = HibernateUtil.buildSessionFactory();
		int res =0;
		
		Query query = session.createNativeQuery(
				"Select active  from newsletter where mobile=" + mobile + " and country_code=" + country_code + " ;");

		try {
		 res = (int) query.getSingleResult();
			return res;

		} catch (NoResultException e) {
			return res;

		}

	}
	public static String RegisterUser( HashMap<String, Object> RegisterMap,HttpServletRequest request,HttpServletResponse response) { 
		  Registration user = null;
          String errMsg = "";
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
	       	     String firstname=(String) RegisterMap.get(Constant.FIRSTNAME);
	             String lastname = (String) RegisterMap.get(Constant.LASTNAME);
	             String email = (String) RegisterMap.get(Constant.EMAIL);
	             String password = (String) RegisterMap.get(Constant.PSW);
	             String confirmPassword = (String) RegisterMap.get(Constant.PSWREPEAT);
	             String remPwd = (String) RegisterMap.get(Constant.REMPWD) == null ? Constant.OFF : (String) RegisterMap.get(Constant.REMPWD);
	             String docpatient = (String) RegisterMap.get(Constant.DOCPATIENT);
	             String acceptTnC =(String) RegisterMap.get(Constant.AcceptTermsAndConditions);
	             String acceptPolicy =(String) RegisterMap.get(Constant.AcceptPolicy);
	             String number = (String) RegisterMap.get(Constant.MOBILE_NUMBER);
	             Long mobile = Long.parseLong(number);
	             System.out.println("docpatient" + docpatient);
	             Integer docOrPatient = determineDocOrPatient(docpatient);
	             Integer rememberPassword = (Constant.OFF.equalsIgnoreCase(remPwd.trim())) ? 0 : 1;

	             Boolean accTerms = Constant.ON.equalsIgnoreCase(acceptTnC);
	             Boolean accPolicy = Constant.ON.equalsIgnoreCase(acceptPolicy);
	             Integer state = 1;
	             Integer age=(Integer)RegisterMap.get(Constant.Age)!= null ? (Integer)RegisterMap.get(Constant.Age) : 0;
	             
	             try { 
	                 if (alreadyExists(email)) {
	                     errMsg = "Email Address already exists in the system";
	                 } else {
	                     user = registerUser(firstname, lastname, password, email, accTerms, docOrPatient, accPolicy, state, rememberPassword, mobile,age);
	                     if (user != null) {
	                         handleSuccessfulRegistration(request, response, user, rememberPassword);
	                     } else {
	                         errMsg = "Error while trying to register New Account";
	                     }
	                 }

	             } catch (Exception e) {
	                 Constant.log("Error processing registration request", 3);
	                 e.printStackTrace();
	                 errMsg = "Internal server error";
	             }finally {
	            	 // Code that will always be executed, whether an exception occurs or not
	            	    // ...
	            	}
	        }catch (Exception e) {
	        	
	        }
	  	     if (errMsg.isEmpty()) {
	  	      Gson gson = new GsonBuilder().serializeNulls().create();
          	      String jsonData = gson.toJson(user);
          	      System.out.println("jsonData"+jsonData);
         	       // Convert JSON string to JSON object
         	       Object jsonObject = gson.fromJson(jsonData, Object.class);
         	       return jsonData;
	 	       }
	        	else
	        	{
	        		return errMsg;
	        	}
	       	
		}

		//Method to handle cookies
	        private static void handleSuccessfulRegistration(HttpServletRequest request, HttpServletResponse response, Registration user, Integer rememberPassword) {
	            Constant.log("User Registered Successfully: " + user.getEmail_address(), 1);
	            request.getSession().setAttribute(Constant.USER, user);
	            CookieManager cookieManager = new CookieManager();
	            Constant.log("Dropping Cookies Now", 0);

	            if (rememberPassword==1) {
	                cookieManager.dropAllCookies(response, user);
	            } else {
	                Constant.log("No Remember Me Flag Selected", 0);
	                cookieManager.dropSessionCookies(response, user);
	            }
	        }
	        private static Registration registerUser(String fName, String lName, String pass, String email, Boolean acceptTerms, Integer docOrPat,
	                Boolean acceptPolicy, Integer state, Integer remPwd, Long mobileNumber,Integer Age) {
	            Registration user = null;
	            try {
	                EnDeCryptor encryptor = new EnDeCryptor();
	                final String secretKey = Constant.SECRETE;
	                String hashedPass = encryptor.encrypt(pass, secretKey);
	                user = saveRegistration(fName, lName, hashedPass, email, acceptTerms, docOrPat, acceptPolicy, state, remPwd, mobileNumber,Age);
	            } catch (Exception e) {
	                Constant.log("Error while trying to register user", 3);
	                e.printStackTrace();
	            }
	            return user;
	        }
	        private static boolean alreadyExists(String email) {
	            Constant.log("Checking if already exists, user with email: " + email, 1);
	            CookieManager cookieManager = new CookieManager();
	            return cookieManager.getUserFromEmailAddress(email) != null;
	        }
	        
	 private static int determineDocOrPatient(String docpatient) {
	    	if (docpatient != null && docpatient.trim().equalsIgnoreCase(Constant.DOCTOR)) {
	    		System.out.println("doctor");
				return 1;
			}else if (docpatient != null && docpatient.trim().equalsIgnoreCase(Constant.PATIENT)) {
				System.out.println("patient");
			return  2;
			}else{
				System.out.println("default");
				//Default to Patient Reg
			return 2;
			}}
}
