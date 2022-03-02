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

//@Deprecated; Use Cookie Manager Instead
public class Cookies {

	public String storeCookiee(String email , String pass){
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
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
				System.out.println(Constant.PREFIX + obj[0]);
				System.out.println(Constant.FIRST_NAME + obj[1]);
				docid= (Integer) obj[0];
				type= (Integer) obj[1];
			}
			cookie=""+docid+"|"+type+"";
		}
//		session.getTransaction().commit();   //session.close();
		return cookie;
	}
	
	public Registration getUserFromPermCookie(String cookieValue){
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		Registration register = new Registration();
		System.out.println(">>>>>>>>>>>>>>>>>>"+ cookieValue);
		//Encryption encrypt = new Encryption();
		String[] result = cookieValue.split(Constant.DefaultStringSplitter);
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
		
		Test decrypt = new Test();
		//String cookie=null;
		//String dd=null;
		final String secretKey = Constant.SECRETE;
		Constant.log("????????????????????????????????????????????????"+regType, 0);
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
				String password = decrypt.decrypt((String)obj[4], secretKey);
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
		return false;
	}
	
	public boolean dropPermCookies(ServletRequest request, Registration user){
		return false;
	}
	
	public boolean dropAllCookies(ServletRequest request, Registration user){
		return false;		
	}
	
	public Cookie constructCookie(String domain, String name, String val, String path, int duration){
		if(domain == null) domain = Constant.DefaultCookieDomain;
		if(name == null) domain = Constant.DefaultSessionCookieName; //make this the name of the session cookie
		if(path == null) path = Constant.DefaultCookiePath;
		Cookie myCookie = new Cookie(name, val);
		myCookie.setDomain(domain);
		myCookie.setMaxAge(duration);
		return myCookie;
	}

}
