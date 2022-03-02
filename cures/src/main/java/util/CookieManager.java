package util;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Doctors;
import model.Registration;

public class CookieManager {

	/* 
	 * This method is redundant; You have the user object where you are calling this from so no need to do another DB lookup
	 * Just use the user object to construct the cookie Value and then construct the cookie;
	 * With the new Cookie Manager Implementation, this method is redundant
	public String storeCookiee(String email , String pass){
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		System.out.println(">>>>>>>>>>>>>>>>>>"+ email);
		int docid=0;
		int type =0;
		String cookie=null;
		//String dd=null;
		Query query = session.createNativeQuery("SELECT registration_id,registration_type FROM registration where email_address = '"+email+"' and pass_word = '"+pass+"';");
		ArrayList<Integer> list=  (ArrayList<Integer>) query.getResultList();
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();
			{
				System.out.println(Constant.PREFEX + obj[0]);
				System.out.println(Constant.FIRST_NAME + obj[1]);
				docid= (Integer) obj[0];
				type= (Integer) obj[1];
			}
			cookie=""+docid+"|"+type+"";
		}
		return cookie;
	}*/
	
	public Registration getUserFromEmailAddress(String emailAddress){
		Session session = HibernateUtil.buildSessionFactory();
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		Registration register = null;
		Constant.log(">>>>>>>>>>>>>>>>>>Looking Up User Based On Email: "+ emailAddress, 1);
		
		Query query = session.createNativeQuery("SELECT * FROM registration where email_address='"+emailAddress+"';");
		ArrayList<Registration> list=  (ArrayList<Registration>)query.getResultList();
		Iterator itr = list.iterator();
		if(itr.hasNext()){
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for Email:"+emailAddress, 1); 
			Object[] obj = (Object[]) itr.next();
			{
				register = new Registration();
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
//		session.getTransaction().commit();   //session.close();
		return register;
	}
	
	public Registration getUserFromPermCookie(String cookieValue){
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		Registration register = new Registration();
		Constant.log("Getting User From Cookie with Cookie Value:"+ cookieValue, 1);
		//Encryption encrypt = new Encryption();
		String[] result = cookieValue.split("\\"+Constant.DefaultStringSplitter);
		String docid = null;
		String type = null;
		//Perm Cookie will always be of the format docid|userType
		for(int i=0; i < result.length; i++){
			docid=result[0];
			type= result[1];
		}
		Integer id = null;
		Integer regType = null;
		if(docid != null)
			id = Integer.parseInt(docid);
		if(type != null)
			regType =Integer.parseInt(type);
		
		//Test decrypt = new Test();
		//String cookie=null;
		//String dd=null;
		final String secretKey = Constant.SECRETE;
		Constant.log("Registration Type:"+regType, 0);
		Query query = session.createNativeQuery("SELECT * FROM registration where registration_id = "+id+" and registration_type= "+regType+";");
		ArrayList<Registration> list=  (ArrayList<Registration>) query.getResultList();
		Iterator itr = list.iterator();
		if(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();
			{
				register.setRegistration_id(obj[0] != null ? (Integer)obj[0] : -1);
				register.setFirst_name(obj[1] != null ? (String)obj[1] : "");
				register.setLast_name((String) obj[2] != null ? (String)obj[2] : "");
				register.setEmail_address((String) obj[3] != null ? (String)obj[3] : "");
				String password = (new EnDeCryptor()).decrypt((String)obj[4], secretKey);
				register.setPass_word((String)password);
				register.setRegistration_type(obj[5] != null ? (Integer)obj[5] : 1);
				register.setAcceptance_condition(obj[6]  != null ? (Boolean)obj[6] : false);
				register.setprivacy_policy(obj[7] != null ? (Boolean)obj[7] : false);
				register.setAccount_state(obj[8] != null ? (Integer)obj[8] : 3);
				register.setRemember_me(obj[9] != null ? (Integer)obj[9] : 0);

				Constant.log(Constant.PREFIX + obj[0], 0);
				Constant.log(Constant.FIRST_NAME + obj[1], 0);
			}
		}
//		session.getTransaction().commit();   //session.close();
		return register;
	}
	
	public boolean dropSessionCookies(ServletResponse response, Registration user){
		try{
			Constant.log("Dropping Session Cookies", 1);
			Cookie defSessCookie = constructCookie(null, Constant.DefaultSessionCookieName, user.getEmail_address()+"|"+user.getRegistration_id()+"|"+user.getRegistration_type(),null, 0);
			((HttpServletResponse)response).addCookie(defSessCookie);
		}catch (Exception e) {
            Constant.log("Error while dropping session cookies: " + e.toString(), 3);
            e.printStackTrace();
            return false;
        }		
		return true;
	}
	
	public boolean dropPermCookies(ServletResponse response, Registration user){
		try{
			Constant.log("Dropping Perm Cookies", 1);
			Cookie defPermCookie = constructCookie(null, Constant.DefaultPermCookieName, user.getRegistration_id()+"|"+user.getRegistration_type(),null, Constant.DefaultPermCookieDuration);
			((HttpServletResponse)response).addCookie(defPermCookie);
		}catch (Exception e) {
            Constant.log("Error while dropping Permanent Cookies: " + e.toString(), 3);
            e.printStackTrace();
            return false;
        }		
		return true;
	}
	
	public boolean dropAllCookies(ServletResponse response, Registration user){
		try{
			Constant.log("Dropping all Cookies", 1);
			dropPermCookies(response, user);
			dropSessionCookies(response, user);
		}catch (Exception e) {
            Constant.log("Error while dropping All Cookies: " + e.toString(), 3);
            e.printStackTrace();
            return false;
        }		
		return true;
	}
	
	public Cookie constructCookie(String domain, String name, String val, String path, int duration){
		if(domain == null) domain = Constant.DefaultCookieDomain;
		if(name == null) name = Constant.DefaultSessionCookieName; //make this the name of the session cookie
		if(path == null) path = Constant.DefaultCookiePath;
		Cookie myCookie = new Cookie(name, val);
		System.out.println("constructCookie>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+name+"domain"+domain);
		myCookie.setDomain(domain);
		myCookie.setPath(path);
		//myCookie.setSecure(true);
		if(duration > 0)
			myCookie.setMaxAge(duration);
		return myCookie;
	}
	
	//Assuming this is to drop Session Cookies
	public Cookie constructSessionCookie(String name, String val){
		Cookie myCookie = constructCookie(Constant.DefaultCookieDomain, name, val, Constant.DefaultCookiePath, 0);
		return myCookie;
	}
	
	//Assuming this is to drop Session Cookies
	public Cookie constructPermCookie(String name, String val){		
		Cookie myCookie = constructCookie(Constant.DefaultCookieDomain, name, val, Constant.DefaultCookiePath, Constant.DefaultPermCookieDuration);
		return myCookie;
	}
	
	public void destroyPermCookie(String name, boolean destroyAllCookies, HttpServletRequest request, HttpServletResponse response){
		Cookie []cookies = request.getCookies();
		String lookingFor = name == null ? Constant.DefaultPermCookieName : name;
		Cookie pCookie = null;
		if(destroyAllCookies)
			lookingFor = null;
		for(int i=0; i<cookies.length; i++){		
			pCookie = cookies[i];
			if(destroyAllCookies){
				pCookie.setDomain(Constant.DefaultCookieDomain);
				pCookie.setMaxAge(0);
				pCookie.setPath(Constant.DefaultCookiePath);
				response.addCookie(pCookie);
			}else if(pCookie.getName().equalsIgnoreCase(lookingFor)){
				pCookie.setDomain(Constant.DefaultCookieDomain);
				pCookie.setMaxAge(0);
				pCookie.setPath(Constant.DefaultCookiePath);
				response.addCookie(pCookie);
				break;
			}
		}		
	}
	
	public void destroySessCookie(String name, boolean destroyAllCookies, HttpServletRequest request, HttpServletResponse response){
		Cookie []cookies = request.getCookies();
		String lookingFor = name == null ? Constant.DefaultSessionCookieName : name;
		Cookie sCookie = null;
		if(destroyAllCookies)
			lookingFor = null;
		for(int i=0; i<cookies.length; i++){		
			sCookie = cookies[i];
			if(destroyAllCookies){
				sCookie.setDomain(Constant.DefaultCookieDomain);
				sCookie.setMaxAge(0);
				sCookie.setPath(Constant.DefaultCookiePath);
				response.addCookie(sCookie);
			}else if(sCookie.getName().equalsIgnoreCase(lookingFor)){
				sCookie.setDomain(Constant.DefaultCookieDomain);
				sCookie.setMaxAge(0);
				sCookie.setPath(Constant.DefaultCookiePath);
				response.addCookie(sCookie);
				break;
			}
		}		
	}
	
	public void destroyAllCookies(HttpServletRequest request, HttpServletResponse response){
		destroySessCookie(null, true, request, response);
		destroyPermCookie(null, true, request, response);
	}

}
