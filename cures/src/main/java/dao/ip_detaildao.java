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

	public static int Insert(Integer id, HttpServletRequest request,Map<String,String> headers,HashMap articleMap) {
	
		SessionFactory factory=new Configuration().configure().buildSessionFactory();
	//	Session session = HibernateUtil.buildSessionFactory();
		//Transaction transaction = session.getTransaction();
	//	session.beginTransaction();
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
		 int reg_id = 0;
		String cookie1=headers.get("cookie");
		if (articleMap.containsKey("Reg_num")) {
			 reg_id=(Integer) articleMap.get("Reg_num");
		}
		
		
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	        Date date = new Date();  
		IP_Details ip =new IP_Details();
		 ip.setArticle_id(id);
		 ip.setDate(formatter.format(date));
		 ip.setIp_address(cookie1);
		 ip.setReg_id(reg_id);
	       Session session1=factory.openSession();
	       Transaction tx=session1.beginTransaction();
	     session1.save(ip);
	       tx.commit();
	    
	       session1.close();
		return 1;
		
		
		
		
	}
	
	
}
