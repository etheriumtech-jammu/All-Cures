package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import util.HibernateUtil;

@Component
public class SubscriptionDaoImpl {

	

	public static int addSubscriptionDetails(HashMap promoMap) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		String subscription_details = (String) promoMap.get("subscription_details");
		int subscription_status = 1;

		String subscription_starttime = null;
		String subscription_endtime = null;
		int price_id = 0;
		String subscription_updatedtime = null;

		if (promoMap.containsKey("subscription_status ")) {
			subscription_status  = Integer.parseInt( (String)promoMap.get("subscription_status ") );
		}
		
		if (promoMap.containsKey("subscription_starttime")) {
			subscription_starttime = (String) promoMap.get("subscription_starttime");
		}
		if (promoMap.containsKey("subscription_endtime")) {
			subscription_endtime = (String) promoMap.get("subscription_endtime");
		}
		if (promoMap.containsKey("price_id")) {
			price_id= Integer.parseInt( (String) promoMap.get("price_id") );
		}
		
			java.util.Date date=new java.util.Date();
			java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
			subscription_updatedtime = sqlDate.toString();
			System.out.println("subscription_updatedtime>>>>>"+subscription_updatedtime);
		
		
		Query query = session
				.createNativeQuery("INSERT INTO `subscription_master`" + " (`subscription_details`,"
						+ " `subscription_status`," + " `subscription_starttime`," + " `subscription_endtime`,"
						+ " `price_id`," + " `subscription_updatedtime`)"
						+ " VALUES" + " ('" + subscription_details + "', " + "  '" + subscription_status + "', " + "  '"
						+ subscription_starttime + "', "+"  '" + subscription_endtime + "'," + " '" + price_id
						+ "'," + " '" +  subscription_updatedtime + "');" + "");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to subscription_master table for subscription_details =  " + subscription_details);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
		
		}

		return ret;
	}
		
	public static ArrayList getAllSubscriptionDetails() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery("SELECT `subscription_master`.`subscription_id`,"
				+ "    `subscription_master`.`subscription_details`," + "    `subscription_master`.`subscription_status`,"
				+ "    `subscription_master`.`subscription_starttime`," + "    `subscription_master`.`subscription_endtime`,"
				
				+ "    `subscription_master`.`price_id`," + "    `subscription_master`.`detailing`,"+ "    `subscription_master`.`plan` "+ "FROM `subscription_master`;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Subscription@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int subscription_id = (int) objects[0];
			String subscription_details = (String) objects[1];
			int subscription_status = (int) objects[2];
			java.sql.Timestamp subscription_starttime = (java.sql.Timestamp) objects[3];
			java.sql.Timestamp subscription_endtime = (java.sql.Timestamp) objects[4];
			int price_id = (int) objects[5];
			String detailing = (String) objects[6];
			String plan= (String) objects[7];



			hm.put("subscription_id", subscription_id);
			hm.put("subscription_details", subscription_details);
			hm.put("subscription_status", subscription_status);
			hm.put("subscription_starttime", subscription_starttime);
			hm.put("subscription_endtime", subscription_endtime);
			hm.put("price_id", price_id);
			hm.put("detailing", detailing);
			hm.put("plan", plan);
			hmFinal.add(hm);
		}
		return (ArrayList) hmFinal;
	}
	


	public static int updateSubscriptionDetails(int subscription_id, HashMap articleMap) {

		Session session = HibernateUtil.buildSessionFactory();

		
		session.beginTransaction();

		String updatestr = "";
		if (articleMap.containsKey("subscription_details")) {
			updatestr += "`subscription_details` = '" + articleMap.get("subscription_details") + "',";
		}
		if (articleMap.containsKey("subscription_status")) {
			updatestr += "`subscription_status` = " + articleMap.get("subscription_status") + ",";
		}
		if (articleMap.containsKey("subscription_starttime")) {
			updatestr += "`subscription_starttime` = '" + articleMap.get("subscription_starttime") + "',";
		}
		if (articleMap.containsKey("subscription_endtime")) {
			updatestr += "`subscription_endtime` = '" + articleMap.get("subscription_endtime") + "',";
		}
		
		if (articleMap.containsKey("price_id")) {
			updatestr += "`price_id` = " + articleMap.get("price_id") + ",";
		}
		
		if (articleMap.containsKey("subscription_updatedtime")) {
			updatestr += "`subscription_updatedtime` = '" + articleMap.get("subscription_updatedtime") + "',";
		}
		
		java.util.Date date=new java.util.Date();
		java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
		String subscription_updatedtime = sqlDate.toString();
		updatestr += " `subscription_updatedtime` = '" + subscription_updatedtime + "',";
		System.out.println("subscription_updatedtime>>>>>"+subscription_updatedtime);
		
	

		updatestr = updatestr.replaceAll(",$", "");
		Query query = session.createNativeQuery(
				"UPDATE `subscription_master`" + "SET" + updatestr + " WHERE `subscription_id` = " + subscription_id + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated subscription_master table for subscription_id =  " + subscription_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
	
		}

		return ret;
	}
	
	
	public static int deleteSubscriptionId(int subscription_id ) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

	
		Query query = session
				.createNativeQuery("UPDATE subscription_master SET subscription_status=0 WHERE subscription_id  = " + subscription_id  + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from subscription_master, where subscription_id  =  " + subscription_id );
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {

		}

		return ret;
	}

	
	public static ArrayList getSubscriptionDetailsById(int subscription_id) {
		
		Session session = HibernateUtil.buildSessionFactory();
		
	
		
		Query query = session.createNativeQuery("SELECT `subscription_master`.`subscription_id`,"
				+ "    `subscription_master`.`subscription_details`," + "    `subscription_master`.`subscription_status`,"
				+ "    `subscription_master`.`subscription_starttime`," + "    `subscription_master`.`subscription_endtime`,"
				
				+ "    `subscription_master`.`price_id`" + "FROM `subscription_master` where subscription_id="+subscription_id+";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Subscription@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
			int subscription_id1 = (int) objects[0];
			String subscription_details = (String) objects[1];
			int subscription_status = (int) objects[2];
			java.sql.Timestamp subscription_starttime = (java.sql.Timestamp) objects[3];
			java.sql.Timestamp subscription_endtime = (java.sql.Timestamp) objects[4];
			int price_id = (int) objects[5];
			
			hm.put("subscription_id1", subscription_id1);
			hm.put("subscription_details", subscription_details);
			hm.put("subscription_status", subscription_status);
			hm.put("subscription_starttime", subscription_starttime);
			hm.put("subscription_endtime", subscription_endtime);
			hm.put("price_id", price_id);
			hmFinal.add(hm);
		}
		return (ArrayList) hmFinal;
	}
		
	

}
