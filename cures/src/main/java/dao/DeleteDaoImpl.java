package dao;

import java.math.BigInteger;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import model.Registration;
import util.HibernateUtil;

public class DeleteDaoImpl {
	public static Integer Delete_Update(Integer usr_id, String reason) {
		
		
		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		int ret = 0;
	
	//	 LocalDateTime now = LocalDateTime.now();  
		java.sql.Timestamp now = new java.sql.Timestamp(new java.util.Date().getTime());
		Query query = session.createNativeQuery(
				"Update registration set Deactivated_time= '" + now + "' ,deactivated=1,reason='"+ reason + "' where registration_id=" + usr_id + " ;");
    
		
		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
		
		
		
		session.getTransaction().commit();
		
		session.close();
		
		return ret;
	}
	
	public static String Login_Delete(String email) {
		Session session = HibernateUtil.buildSessionFactory();

		String result="";
			
		Query query = session.createNativeQuery(
				"Select registration.deactivated,CAST(last_login_datetime AS DATE) ,CAST(Deactivated_time AS DATE) from registration where email_address='" + email + "'");
		session.beginTransaction();
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		if (results.size() == 0)
		{
			result="Account does not  exist";
		}
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			
			Integer deactivated = (Integer) objects[0];
			System.out.println(deactivated);
			Date date1 = (Date) objects[1];
			Date date2 = (Date) objects[2];
			
			if(deactivated !=null)
			{
			if (deactivated == 1)
			{
				Date today=new Date();
				
				long deactivationDiff=today.getTime()-date2.getTime();
				long daysSinceDeactivation = Math.round(deactivationDiff/(1000*3600*24));
				
				 int result1 = date1.compareTo(date2);
				    if (result1 < 0 && daysSinceDeactivation >= 30 )   {
				    	
				      System.out.println(date1 + " is before " + date2);
				     
				      result="Account is deleted";
				    } 
				    
				    else
				    {
				    	result="Account is deactivated";
				    }
			}
			}
			    else 
			    {
			    	result="Account exists";
			    }
			

		}
		
		session.getTransaction().commit();

		session.close();
		
	return result;
	
	}
	
	}

