package dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.util.StringUtils;

import model.*;
import util.HibernateUtil;

public class ip_detaildao {

	public static int Insert(Integer id, String add,Integer reg) {
	
		SessionFactory factory=new Configuration().configure().buildSessionFactory();
	//	Session session = HibernateUtil.buildSessionFactory();
		//Transaction transaction = session.getTransaction();
	//	session.beginTransaction();
		System.out.println(add);
		
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	        Date date = new Date();  
		IP_Details ip =new IP_Details();
		 ip.setArticle_id(id);
		 ip.setDate(formatter.format(date));
		 ip.setIp_address(add);
		 ip.setReg_id(reg);
	       Session session=factory.openSession();
	       Transaction tx=session.beginTransaction();
	     session.save(ip);
	       tx.commit();
	    
	       session.close();
		return 1;
		
		
		
		
	}
	
	
}
