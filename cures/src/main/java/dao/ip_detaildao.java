package dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.util.StringUtils;

import model.*;
import util.Constant;
import util.HibernateUtil;

public class ip_detaildao {

	public static Integer Insert(Integer article_id, HttpServletRequest request,Integer user_id,String cookie,String whats_app) {
	
		SessionFactory factory=new Configuration().configure().buildSessionFactory();
	//	Session session = HibernateUtil.buildSessionFactory();
		//Transaction transaction = session.getTransaction();
	//	session.beginTransaction();
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
	
	//	String cookie1=headers.get("cookie");
		System.out.println(cookie);
		IP_Details ip =new IP_Details();
		if (whats_app.equals("whatsapp"))
		{
			 ip.setInfo(whats_app);
		}
		
		else
		{
			 ip.setInfo("NA");
		}
		
//		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	        Date date = new Date();  
		
		 ip.setArticle_id(article_id);
		 ip.setDate(formatter.format(date));
		 ip.setCookie_list(cookie);
		 ip.setReg_id(user_id);
		 Session session1=factory.openSession();
	       Transaction tx=session1.beginTransaction();
	     session1.save(ip);
	       tx.commit();
	    
	       session1.close();
		
		return 1;
		
		
	}
	
	
}
