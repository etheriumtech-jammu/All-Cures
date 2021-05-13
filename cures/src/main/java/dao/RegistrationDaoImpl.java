package dao;




import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Doctors;
import model.Patient;
import model.Registration;
import util.Constant;
import util.HibernateUtil;

public class RegistrationDaoImpl {


	public Registration saveRegistration(String f_name,String l_name, String pwd, String email, Boolean accept, Integer type, 
			Boolean policy,Integer state, Integer rem) {
		// creating seession factory object

		Registration user = null;
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();
		
		Constant.log("Registering User with Firstname to DB:"+f_name, 0);
		/*
		 * TODO: Fix This Registration Logic. This is how its supposed to work
		 * For Any New User (Doctor or Patient) trying to register, Match the Email Address Provided during registration with the Email Address 
		 * that we have on file for that doctor/patient
		 * If this is a doctor
		 * 		Create a new entry in the registration table with email, password, fname, lname etc. 
		 * 		Use the RegistrationID (auto-increment) from the Registration Table to be the docid in the Doctors table
		 * 		use the first name, last name etc. from the signup page and update the doctors table with these 3 fields:
		 * 			docid = registrationid, firstname, last name
		 * 
		 * If this is a patient
		 * 		Create a new entry in the registration table with email, password, fname, lname etc.
		 *		Use the RegistrationID (auto-increment) from the Registration Table to be the patientid in the Patient table
		 * 		use the first name, last name etc. from the signup page and update the patient table with these 3 fields:
		 * 			patientid = registrationid, firstname, last name
		 * 
		 * If we were to ever invite a doctor (or consumer) to register, it would have to be based purely on email addresses as we know that we 
		 * will not have a docid (or patientid) till someone registers
		 * 
		 * 		
		*/
		try {
			session.getTransaction().begin();

			Registration reg = new Registration();

			DoctorsDaoImpl doctor;
			PatientDaoImpl patient;
			//doctor.saveDoctors(f_name, l_name, email);
			//Integer docid = doctor.findAllDoctorsBydocid(email, f_name, l_name);
			//System.out.println("$$$$$$$$$$$$$$$$$$$$"+docid);

			//Create a New Row in the Registration Table
			
			reg.setEmail_address(email);
			reg.setPass_word(pwd);
			reg.setFirst_name(f_name);
			reg.setLast_name(l_name);
			reg.setAcceptance_condition(accept);
			reg.setRegistration_type(type);
			reg.setprivacy_policy(policy);
			reg.setAccount_state(state);
			reg.setRemember_me(rem);
			session.save(reg);
			session.getTransaction().commit();
			//session.close();
			//LOGIC Q: Right now we are setting the registration id as the docid for the doctors table or patientid for the patient table
			//		   What will happen when we do mass doctor updates; There is a possibility that the doctors created as a result that are 
			//		   not registered with us might have a docid issued that is not the same as the registration id; Unless we make the docid
			//		   and patientid in the doctors and patients table respectively a non-required field and only once registration is done do 
			//		   you get assigned an id (docid or patientid) and till then do not have to carry it; Makes a lot of sense to do it that
			//		   way
			//TODO: Make DocId in Doctors table and PatientId in Patients table a non required field and also foreign keyed into the Registration 
			// 		table. 
			//Create a Row in either the Doctors or Patients Table with the Same RegId
			
			if(type==1){
				Constant.log("Registering User is a Doctor", 0);
				RegistrationDaoImpl registerDao = new RegistrationDaoImpl();
				user =	registerDao.findUserByEmail(email);
				doctor = new DoctorsDaoImpl();
				doctor.saveDoctors(user.getRegistration_id(), f_name, l_name, email);
				// sessionFactory.close();
				//Now that the doctor is signed up, should we log her in as well?
				//TODO: LogUserIn
			}else{
				Constant.log("Registering User is a Patient", 0);
				RegistrationDaoImpl registerDao = new RegistrationDaoImpl();
				user = registerDao.findUserByEmail(email);
				patient = new PatientDaoImpl();
				patient.savePatient(user.getRegistration_id(), f_name, l_name, email);
				// sessionFactory.close();		
				//TODO: LogUserIn
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.getTransaction().rollback();
		}
		return user;
	}
//Used for Login Lookup
	public static Registration findAllUsers(String email, String pwd) {
		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();
		// creating session object
		Session session = factory.getCurrentSession();
		//Only Logging Password in Logs in Debug Mode
		Constant.log("Finding users with email:"+email+" and pass: " + pwd, 0);
		
		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		Registration register = null;
		Query query = session.createNativeQuery("select registration_id, first_name, last_name, email_address, pass_word, "
				+ "registration_type, acceptance_condition, privacy_policy, account_state, remember_me from registration "
				+ "where email_address='"+email+"' and pass_word='"+pwd+"'");
				
		ArrayList<Registration> regList =  (ArrayList<Registration>)query.getResultList();
		Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:"+email, 1);
		Iterator itr = regList.iterator();
		if(itr.hasNext()){
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:"+email, 1); 
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer)obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String)obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String)obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String)obj[3] : "");
				//Security Best Practice: Do not put password in the user obj in session
				//String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				//register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer)obj[5] : 1);
				register.setAcceptance_condition(obj[6]  != null ? (Boolean)obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean)obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer)obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer)obj[9] : 0);
				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}		
		return  register;
	}

	public static Registration findUserByEmail(String email) {
		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>"+ email), 0);
		int docid=0;
		
		Registration register = null;
		Query query = session.createNativeQuery("select * from registration where email_address='"+email.trim()+"'");
		ArrayList<Registration> list=  (ArrayList<Registration>)query.getResultList();
		Iterator itr = list.iterator();
		if(itr.hasNext()){
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:"+email, 1); 
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer)obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String)obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String)obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String)obj[3] : "");
				//Security Best Practice: Do not put password in the user obj in session
				//String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				//register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer)obj[5] : 1);
				register.setAcceptance_condition(obj[6]  != null ? (Boolean)obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean)obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer)obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer)obj[9] : 0);
				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}		
		return  register;
	}

	public static Registration findUserByRegId(int regid) {
		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>FINDING USER FOR ID:"+ regid), 0);
		int docid=0;
		
		Registration register = null;
		Query query = session.createNativeQuery("select * from registration where registration_id="+regid);
		ArrayList<Registration> list=  (ArrayList<Registration>)query.getResultList();
		Iterator itr = list.iterator();
		if(itr.hasNext()){
			register = new Registration();
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for ID:"+regid, 1); 
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer)obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String)obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String)obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String)obj[3] : "");
				//Security Best Practice: Do not put password in the user obj in session
				//String password = EnDeCryptor.decrypt((String)obj[4], secretKey);
				//register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer)obj[5] : 1);
				register.setAcceptance_condition(obj[6]  != null ? (Boolean)obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean)obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer)obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer)obj[9] : 0);
				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}		
		return  register;
	}

}
