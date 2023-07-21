package dao;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.io.IOException;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import Chat_Function.NotificationService;
import util.HibernateUtil;
import util.WhatsAPITemplateMessage;
import com.google.firebase.messaging.FirebaseMessagingException;

@Component
public class TipDaoImpl {

		public static int addTipDetails(int user_id,HashMap promoMap) throws FirebaseMessagingException, IOException {
				
		Session session = HibernateUtil.buildSessionFactory();

		
		String tip_title = (String) promoMap.get("tip_title");
		int ret = 0;
		String tip_date = null;
		int tip_status = 1;
		String tip_updatedtime = null;
		Integer article_id=(Integer) promoMap.get("article_id");
		Integer pubstatus_id=0;
		java.util.Date date=new java.util.Date();
		java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
		tip_date = sqlDate.toString();
		System.out.println("start_time>>>>>"+tip_date);
		
		
		java.util.Date dates=new java.util.Date();
		java.sql.Timestamp sqlDates=new java.sql.Timestamp(date.getTime());
		tip_updatedtime = sqlDate.toString();
		System.out.println("tip_updatedtime>>>>>"+tip_updatedtime);
		
		Query query1= session.createNativeQuery("Select title,pubstatus_id from article where article_id = " + article_id);
		List<Object[]> results = (List<Object[]>) query1.getResultList();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			String title = (String) objects[0];

			 pubstatus_id = (Integer) objects[1];

		}
		session.beginTransaction();

		if (pubstatus_id ==3)
		{
          Query query = session
				.createNativeQuery("INSERT INTO `tip`" + " (`tip_title`,"
						+ " `tip_date`,"+"`user_id`,"+"`tip_status`,"+"`tip_updatedtime` , "+"`artticle_id`)"
						+ " VALUES" + " ('" + tip_title + "',    " + "     '" + tip_date + "', " + " '"+ user_id +"', " + " '"+tip_status+"', " + " '"+tip_updatedtime+"' , " + " '"+article_id+"' );" + "");
		
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to tip table for tip_title =  " + tip_title);
			
			
			
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
		
		}
		try {
			WhatsAPITemplateMessage.POSTRequestTrackEventsByTip(tip_title);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TipNotification(tip_title);
	/*	}
		
		else
		{
			System.out.println("Article is not published");
		}
	*/
		return ret;
		
	}
	
		public static ArrayList getAllTipDetails() {

			Session session = HibernateUtil.buildSessionFactory();

			Query query = session.createNativeQuery("SELECT `tip`.`tip_id`,"
					+ "    `tip`.`tip_title`," + "    `tip`.`tip_date`,"
					+ "    `tip`.`user_id`," + "    `tip`.`tip_status`"+ "FROM `tip`;");
			
			List<Object[]> results = (List<Object[]>) query.getResultList();
			System.out.println("result list Subscription@@@@@@@@@@@@@ size=" + results.size());
			List hmFinal = new ArrayList();
			for (Object[] objects : results) {
				HashMap hm = new HashMap();
				int tip_id = (int) objects[0];
				String tip_title = (String) objects[1];
				java.sql.Timestamp tip_date= (java.sql.Timestamp) objects[2];
				int user_id = (int) objects[3];
				int tip_status = (int) objects[4];


				hm.put("tip_id", tip_id);
				hm.put("tip_title", tip_title);
				hm.put("tip_date", tip_date);
				hm.put("user_id", user_id);
				hm.put("tip_status", tip_status);
				hmFinal.add(hm);
			}
			return (ArrayList) hmFinal;
		}
		

//	public static ArrayList getAllTipDetails() {
//
//		Session session = HibernateUtil.buildSessionFactory();
//
//		Query query = session.createNativeQuery("SELECT `tip`.`tip_id`,"
//				+ "    `tip`.`tip_title`," + "    `tip`.`tip_date`,"
//				+ "    `tip`.`user_id`," + "    `tip`.`tip_status` "+ "FROM `tip`;");
//		
//		
//		List<Object[]> results = (List<Object[]>) query.getResultList();
//		System.out.println("result list Subscription@@@@@@@@@@@@@ size=" + results.size());
//		List hmFinal = new ArrayList();
//		for (Object[] objects : results) {
//			HashMap hm = new HashMap();
//			int tip_id = (int) objects[0];
//			String tip_title = (String) objects[1];
//			java.sql.Timestamp tip_date = (java.sql.Timestamp) objects[2];
//			int user_id = (int) objects[3];
//			int tip_status = (int) objects[4];
//
//
//			hm.put("tip_id", tip_id);
//			hm.put("tip_title", tip_title);
//			hm.put("tip_date", tip_date);
//			hm.put("user_id", user_id);
//			hm.put("tip_status", tip_status);
//			
//			hmFinal.add(hm);
//		}
//		return (ArrayList) hmFinal;
//	}
	
	public static int updateTipDetails(int tip_id, HashMap articleMap) {

		Session session = HibernateUtil.buildSessionFactory();

		
		session.beginTransaction();

		String updatestr = "";
		if (articleMap.containsKey("tip_title")) {
			updatestr += "`tip_title` = '" + articleMap.get("tip_title") + "',";
		}
		
		if (articleMap.containsKey("user_id")) {
			updatestr += "`user_id` = '" + articleMap.get("user_id") + "',";
		}
		
		if (articleMap.containsKey("tip_updatedtime")) {
			updatestr += "`tip_updatedtime` = '" + articleMap.get("tip_updatedtime") + "',";
		}
		
		java.util.Date date=new java.util.Date();
		java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
		String tip_updatedtime = sqlDate.toString();
		updatestr += " `tip_updatedtime` = '" + tip_updatedtime + "',";
		System.out.println("tip_updatedtime>>>>>"+tip_updatedtime);

		updatestr = updatestr.replaceAll(",$", "");
		Query query = session.createNativeQuery(
				"UPDATE `tip`" + "SET" + updatestr + " WHERE `tip_id` = " + tip_id + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated tip table for tip_id =  " + tip_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
	
		}

		return ret;
	}  
	
	public static int deleteTipId(int tip_id ) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

	
		Query query = session
				.createNativeQuery("UPDATE tip SET tip_status=0 WHERE tip_id  = " + tip_id  + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from subscription_master, where tip_id  =  " + tip_id );
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {

		}

		return ret;
	}
	
public static ArrayList getTipDetailsById(int tip_id) {
		
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("SELECT `tip`.`tip_id`,"
				+ "    `tip`.`tip_title`," + "    `tip`.`tip_date`,"
				+ "    `tip`.`user_id`," + "    `tip`.`tip_status`" + "FROM `tip` where tip_id="+tip_id+";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Subscription@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
			int tip_id1 = (int) objects[0];
			String tip_title = (String) objects[1];
			java.sql.Timestamp tip_date= (java.sql.Timestamp) objects[2];
			int user_id = (int) objects[3];
			int tip_status = (int) objects[4];


			hm.put("tip_id1", tip_id);
			hm.put("tip_title", tip_title);
			hm.put("tip_date", tip_date);
			hm.put("user_id", user_id);
			hm.put("tip_status", tip_status);
			hmFinal.add(hm);
		}
		return (ArrayList) hmFinal;
	}

	public static void TipNotification(String tip_title) throws FirebaseMessagingException, IOException
		{
			String title="Tip of the Day";
			String action="Tip";
			String id="";
			List<String> recipientTokens = FCMDao.getTokens();
			NotificationService.sendNotification(recipientTokens,title,  tip_title,action,id);
			System.out.println("Message Sent");
						
		}

}
