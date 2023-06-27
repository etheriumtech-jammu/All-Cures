package dao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UserController;
import model.EmailDTO;
import model.Registration;
import service.SendEmailService;
import util.Constant;
import util.HibernateUtil;
import util.WhatsAPITrackUsersSync;
import util.WhatsAPITrackUsers;

@Component
public class RegistrationDaoImpl {

	@Autowired
	private SendEmailService emailUtil;

	public Registration saveRegistration(String f_name, String l_name, String pwd, String email, Boolean accept,
			Integer type, Boolean policy, Integer state, Integer rem, Long mobile) {
		// creating seession factory object

		Registration user = null;
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		session.beginTransaction();

		Constant.log("Registering User with Firstname to DB:" + f_name, 0);
		/*
		 * TODO: Fix This Registration Logic. This is how its supposed to work For Any
		 * New User (Doctor or Patient) trying to register, Match the Email Address
		 * Provided during registration with the Email Address that we have on file for
		 * that doctor/patient If this is a doctor Create a new entry in the
		 * registration table with email, password, fname, lname etc. Use the
		 * RegistrationID (auto-increment) from the Registration Table to be the docid
		 * in the Doctors table use the first name, last name etc. from the signup page
		 * and update the doctors table with these 3 fields: docid = registrationid,
		 * firstname, last name
		 * 
		 * If this is a patient Create a new entry in the registration table with email,
		 * password, fname, lname etc. Use the RegistrationID (auto-increment) from the
		 * Registration Table to be the patientid in the Patient table use the first
		 * name, last name etc. from the signup page and update the patient table with
		 * these 3 fields: patientid = registrationid, firstname, last name
		 * 
		 * If we were to ever invite a doctor (or consumer) to register, it would have
		 * to be based purely on email addresses as we know that we will not have a
		 * docid (or patientid) till someone registers
		 * 
		 * 
		 */
		try {
			//session.getTransaction().begin();

			Registration reg = new Registration();

			DoctorsDaoImpl doctor;
			PatientDaoImpl patient;
			// doctor.saveDoctors(f_name, l_name, email);
			// Integer docid = doctor.findAllDoctorsBydocid(email, f_name, l_name);
			// System.out.println("$$$$$$$$$$$$$$$$$$$$"+docid);

			// Create a New Row in the Registration Table

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
			session.save(reg);
//			session.getTransaction().commit();
			session.getTransaction().commit();   //session.close();
			// LOGIC Q: Right now we are setting the registration id as the docid for the
			// doctors table or patientid for the patient table
			// What will happen when we do mass doctor updates; There is a possibility that
			// the doctors created as a result that are
			// not registered with us might have a docid issued that is not the same as the
			// registration id; Unless we make the docid
			// and patientid in the doctors and patients table respectively a non-required
			// field and only once registration is done do
			// you get assigned an id (docid or patientid) and till then do not have to
			// carry it; Makes a lot of sense to do it that
			// way
			// TODO: Make DocId in Doctors table and PatientId in Patients table a non
			// required field and also foreign keyed into the Registration
			// table.
			// Create a Row in either the Doctors or Patients Table with the Same RegId

			if (type == 1) {
				Constant.log("Registering User is a Doctor", 0);
				RegistrationDaoImpl registerDao = new RegistrationDaoImpl();
				user = registerDao.findUserByEmail(email);
				doctor = new DoctorsDaoImpl();
				doctor.saveDoctors(user.getRegistration_id(), f_name, l_name, email);
				Long rowno = new DoctorsDaoImpl().findDoctorsByEmail(email).getRowno();
				user.setRowno(rowno);
				// sessionFactory.close();
				// Now that the doctor is signed up, should we log her in as well?
				// TODO: LogUserIn
			} else {
				Constant.log("Registering User is a Patient", 0);
				RegistrationDaoImpl registerDao = new RegistrationDaoImpl();
				user = registerDao.findUserByEmail(email);
				patient = new PatientDaoImpl();
				patient.savePatient(user.getRegistration_id(), f_name, l_name, email);
				// sessionFactory.close();
				// TODO: LogUserIn
			}

			EmailDTO emaildto = new EmailDTO();

//			emaildto.setTo(email);
//			emaildto.setSubject("Registration user email..");
//			emaildto.setEmailtext("Hi " + f_name + "," + " Thanks for the registration with allcures.");
//			EmailDTO emaildto2 = new EmailDTO();

			emaildto.setTo(email);
//			emaildto.setFrom("All-Cures INFO");
			emaildto.setSubject("Registration User: All-Cures ");
			// Populate the template data
			Map<String, Object> templateData = new HashMap<>();
			templateData.put("templatefile", "email/registration.ftlh");
			templateData.put("first_name", f_name);
			// String link = "http://localhost:3000";
			String link = "https://all-cures.com";
			templateData.put("linkverfiy", link);

			// object -> Map
			ObjectMapper oMapper = new ObjectMapper();
			Map<String, Object> mapUser = oMapper.convertValue(user, Map.class);
			templateData.putAll(mapUser);
			emaildto.setEmailTemplateData(templateData);
			System.out.println(emaildto);

			String returnEmail = emailUtil.shootEmail(emaildto);

			// new SendEmailUtil().shootEmail(email, "Welcome Allcures",
			// "Hi " + f_name + "," + " Thanks for the registration with allcures.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.getTransaction().rollback(); //session.getTransaction().rollback();
		}
		return user;
	}

//Used for Login Lookup
	public static Registration findAllUsers(String email, String pwd) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// Only Logging Password in Logs in Debug Mode
		Constant.log("Finding users with email:" + email + " and pass: " + pwd, 0);

		// creating transaction object
//		session.beginTransaction();
		Registration register = null;
		Query query = session
				.createNativeQuery("select registration_id, first_name, last_name, email_address, pass_word, "
						+ "registration_type, acceptance_condition, privacy_policy, account_state, remember_me, login_attempt,last_login_datatime"
						+ " ,(select rowno from doctors where docid = registration_id) as rowno from registration "
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
				register.setRowno(obj[12] != null ? (Long) Long.valueOf( obj[12]+"" ): 0);
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

		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>" + email), 0);
		int docid = 0;

		Registration register = null;
		Query query = session
				.createNativeQuery("select * from registration where email_address='" + email.trim() + "'");
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

		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>FINDING USER FOR ID:" + regid), 0);
		int docid = 0;

		Registration register = null;
		Query query = session.createNativeQuery("select registration_id, first_name, last_name, email_address, pass_word, registration_type,"
				+ " acceptance_condition, privacy_policy, account_state, remember_me, last_login_datetime, login_attempt, last_login_datatime,"
				+ " concat(\"\",mobile_number), rowno from registration where registration_id=" + regid);
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
				register.setMobile_number( obj[13] != null ? Long.parseLong((String) obj[13]) : 0);
//				register.Double setMobile_number = (Double) objects[13];

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
		//Session session = factory;
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

	public int checkEmail(String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
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
//				emaildto2.setFrom("All-Cures INFO");
				emaildto2.setSubject("Forgot password: All-Cures");
				// Populate the template data
				Map<String, Object> templateData = new HashMap<>();
				templateData.put("templatefile", "email/forgotpassword.ftlh");
				templateData.put("name", email);
				templateData.put("linkforgotpassword", link);
				emaildto2.setEmailTemplateData(templateData);

				String returnEmail = emailUtil.shootEmail(emaildto2);
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
		//Session session = factory;
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
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static int resetLoginDetails(int regId) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		java.sql.Timestamp last_login_datetime = new java.sql.Timestamp(new java.util.Date().getTime());
		Query query = session
				.createNativeQuery("update registration set login_attempt = 0 \r\n" + " ,deactivated=0,deactivated_time=NULL,reason_id=NULL, last_login_datetime = '"
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
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static String subscribe(long mobile, HashMap ns_map) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		int result=checkSubscription(mobile,(String)ns_map.get("country_code"));
		if(result == 1 )
		{
		return("Already subscribed");
		}
		else {
		// creating session object
		//Session session = factory;
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
				+ ",\r\n'" + nl_subscription_cures_id + "',1, "+country_code+");\r\n");
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
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return "Subscribed";
		}
	}

	public static int updatesubscribe(long mobile, HashMap ns_map) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
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
		//Session session = factory;
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
		//Session session = factory;
		// Only Logging Password in Logs in Debug Mode
		Constant.log("Finding users with mobile:" + mobile, 0);

		// creating transaction object
//		session.beginTransaction();
		Registration register = null;
		Query query = session.createNativeQuery(
				"SELECT `newsletter`.`user_id`,\r\n" + "    `newsletter`.`nl_subscription_disease_id`,"
						+ "    `newsletter`.`nl_start_date`,\r\n" + "    `newsletter`.`nl_sub_type`,\r\n"
						+ "    `newsletter`.`mobile`,\r\n" + "    `newsletter`.`nl_subscription_cures_id`,\r\n"
						+ "    `newsletter`.`active`,\r\n" + "    `newsletter`.`nl_end_date`,\r\n"
						+ "    `newsletter`.`country_code`, " 
						+ "(select dc_name from disease_condition dc where dc.dc_id in ( `newsletter`.`nl_subscription_disease_id` )) as disease_name"
						+ " FROM `newsletter`\r\n"
						+ " where mobile=" + mobile + " and country_code=" + country_code + ";");

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
}
