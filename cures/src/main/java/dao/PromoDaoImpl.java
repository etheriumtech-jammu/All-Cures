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
public class PromoDaoImpl {

	public static ArrayList getAllPromoDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("SELECT `promo_master`.`promo_id`,\r\n"
				+ "    `promo_master`.`promo_code`,\r\n" + "    `promo_master`.`promo_start_datetime`,\r\n"
				+ "    `promo_master`.`promo_end_datetime`,\r\n" + "    `promo_master`.`promo_max_limit`,\r\n"
				+ "    `promo_master`.`promo_updated_by`,\r\n" + "    `promo_master`.`promo_updated_date`,\r\n"
				+ "    `promo_master`.`promo_active`\r\n" + "FROM `promo_master`;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Promo@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int promo_id = (int) objects[0];
			String promo_code = (String) objects[1];
			java.sql.Timestamp promo_start_datetime = (java.sql.Timestamp) objects[2];
			java.sql.Timestamp promo_end_datetime = (java.sql.Timestamp) objects[3];
			int promo_max_limit = (int) objects[4];
			int promo_updated_by = (int) objects[5];
			java.sql.Timestamp promo_updated_date = (java.sql.Timestamp) objects[6];
			Integer promo_active = (Integer) objects[7];

			hm.put("promo_id", promo_id);
			hm.put("promo_code", promo_code);
			hm.put("promo_start_datetime", promo_start_datetime);
			hm.put("promo_end_datetime", promo_end_datetime);
			hm.put("promo_max_limit", promo_max_limit);
			hm.put("promo_updated_by", promo_updated_by);
			hm.put("promo_updated_date", promo_updated_date);
			hm.put("promo_active", promo_active);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return (ArrayList) hmFinal;
	}
	public static ArrayList getPromoDetailsById(int promo_id) {
		
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		
		// creating session object
		//Session session = factory;
		
		// creating transaction object
//		session.beginTransaction();
		
		Query query = session.createNativeQuery("SELECT `promo_master`.`promo_id`,\r\n"
				+ "    `promo_master`.`promo_code`,\r\n" + "    `promo_master`.`promo_start_datetime`,\r\n"
				+ "    `promo_master`.`promo_end_datetime`,\r\n" + "    `promo_master`.`promo_max_limit`,\r\n"
				+ "    `promo_master`.`promo_updated_by`,\r\n" + "    `promo_master`.`promo_updated_date`,\r\n"
				+ "    `promo_master`.`promo_active`\r\n" + "FROM `promo_master` where promo_id="+promo_id+";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list Promo@@@@@@@@@@@@@ size=" + results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int promo_id1 = (int) objects[0];
			String promo_code = (String) objects[1];
			java.sql.Timestamp promo_start_datetime = (java.sql.Timestamp) objects[2];
			java.sql.Timestamp promo_end_datetime = (java.sql.Timestamp) objects[3];
			int promo_max_limit = (int) objects[4];
			int promo_updated_by = (int) objects[5];
			java.sql.Timestamp promo_updated_date = (java.sql.Timestamp) objects[6];
			Integer promo_active = (Integer) objects[7];
			
			hm.put("promo_id", promo_id1);
			hm.put("promo_code", promo_code);
			hm.put("promo_start_datetime", promo_start_datetime);
			hm.put("promo_end_datetime", promo_end_datetime);
			hm.put("promo_max_limit", promo_max_limit);
			hm.put("promo_updated_by", promo_updated_by);
			hm.put("promo_updated_date", promo_updated_date);
			hm.put("promo_active", promo_active);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return (ArrayList) hmFinal;
	}

	public static int addPromoDetails(HashMap promoMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String promo_code = (String) promoMap.get("promo_code");
		String promo_start_datetime = null;
		String promo_end_datetime = null;
		int promo_max_limit = 0;
		int promo_updated_by = 0;
		String promo_updated_date = null;
		int promo_active = 0;// by default promo is not active

		if (promoMap.containsKey("promo_start_datetime")) {
			promo_start_datetime = (String) promoMap.get("promo_start_datetime");
		}
		if (promoMap.containsKey("promo_end_datetime")) {
			promo_end_datetime = (String) promoMap.get("promo_end_datetime");
		}
		if (promoMap.containsKey("promo_max_limit")) {
			promo_max_limit = Integer.parseInt( (String) promoMap.get("promo_max_limit") );
		}
		if (promoMap.containsKey("promo_updated_by")) {
			promo_updated_by = Integer.parseInt( (String)promoMap.get("promo_updated_by") );
		}
//		if (promoMap.containsKey("promo_updated_date")) {
//			promo_updated_date = (String) promoMap.get("promo_updated_date");
			java.util.Date date=new java.util.Date();
			java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
			promo_updated_date = sqlDate.toString();
			System.out.println("promo_updated_date>>>>>"+promo_updated_date);
		//}
		if (promoMap.containsKey("promo_active")) {
			promo_active = Integer.parseInt( (String)promoMap.get("promo_active") );
		}
		Query query = session
				.createNativeQuery("INSERT INTO `promo_master`\r\n" + " (`promo_code`,\r\n"
						+ " `promo_start_datetime`,\r\n" + " `promo_end_datetime`,\r\n" + " `promo_max_limit`,\r\n"
						+ " `promo_updated_by`,\r\n" + " `promo_updated_date`,\r\n" + " `promo_active`)\r\n"
						+ " VALUES\r\n" + " ('" + promo_code + "',\r\n" + " '" + promo_start_datetime + "',\r\n" + " '"
						+ promo_end_datetime + "',\r\n" + " " + promo_max_limit + ",\r\n" + " " + promo_updated_by
						+ ",\r\n" + " '" + promo_updated_date + "',\r\n" + " " + promo_active + ");\r\n" + "");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to promo_master table for promo_code =  " + promo_code);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static int updatePromoDetails(int promo_id, HashMap articleMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String updatestr = "";
		if (articleMap.containsKey("promo_code")) {
			updatestr += "`promo_code` = '" + articleMap.get("promo_code") + "',\r\n";
		}
		if (articleMap.containsKey("promo_max_limit")) {
			updatestr += "`promo_max_limit` = " + articleMap.get("promo_max_limit") + ",\r\n";
		}
		if (articleMap.containsKey("promo_start_datetime")) {
			updatestr += "`promo_start_datetime` = '" + articleMap.get("promo_start_datetime") + "',\r\n";
		}
		if (articleMap.containsKey("promo_end_datetime")) {
			updatestr += "`promo_end_datetime` = '" + articleMap.get("promo_end_datetime") + "',\r\n";
		}
		if (articleMap.containsKey("promo_updated_date")) {
			updatestr += "`promo_updated_date` = '" + articleMap.get("promo_updated_date") + "',\r\n";
		}
		if (articleMap.containsKey("promo_updated_by")) {
			updatestr += "`promo_updated_by` = " + articleMap.get("promo_updated_by") + ",\r\n";
		}
		
		java.util.Date date=new java.util.Date();
		java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
		String promo_updated_date = sqlDate.toString();
		updatestr += " `promo_updated_date` = '" + promo_updated_date + "',\r\n";
		System.out.println("promo_updated_date>>>>>"+promo_updated_date);
		
		if (articleMap.containsKey("promo_active")) {
			updatestr += "`promo_active` = " + articleMap.get("promo_active") + ",\r\n";
		}

		updatestr = updatestr.replaceAll(",$", "");
		Query query = session.createNativeQuery(
				"UPDATE `promo_master`\r\n" + "SET\r\n" + updatestr + " WHERE `promo_id` = " + promo_id + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated promo_master table for promo_id =  " + promo_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static int deletePromoId(int promo_id) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
		session.beginTransaction();

		// Query query = session.createNativeQuery("DELETE FROM ARTICLE WHERE ARTICLE_ID
		// = " + article_id + ";");
		// SOFT delte done instead of hard delete form database
		Query query = session
				.createNativeQuery("UPDATE promo_master SET promo_active=0 WHERE promo_id = " + promo_id + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from promo_master, where promo_id =  " + promo_id);
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public int setPromoPaidStage(HashMap articlePromoIds, int reviewed_by) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		String paidArticleIds = (String) articlePromoIds.get("articles_promo_paid");
		String unpaidArticleIds = (String) articlePromoIds.get("articles_promo_unpaid");

		System.out.println(articlePromoIds);
		Query queryArticlePromoPaid = session
				.createNativeQuery("UPDATE article SET promo_stage=1  WHERE article_id in ( " + paidArticleIds + " );");
		Query queryArticlePromoUnpaid = session.createNativeQuery(
				"UPDATE article SET promo_stage=0  WHERE article_id in ( " + unpaidArticleIds + " );");
		int ret = 0;
		try {
			ret = queryArticlePromoPaid.executeUpdate();
			ret = queryArticlePromoUnpaid.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated article table for promo_id  =  " + paidArticleIds + " ,promo_stage=0 (unpaid)");
			System.out.println("updated article table for promo_id  =  " + unpaidArticleIds + " ,promo_stage=1(paid)");

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public int setPromoPaidStage(HashMap articlePromoIds, int reviewed_by, int stage) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		String articleIds = (String) articlePromoIds.get("articles_ids");

		System.out.println(articlePromoIds);
		Query queryArticlePromoPaid = session.createNativeQuery(
				"UPDATE article SET promo_stage=" + stage + "  WHERE article_id in ( " + articleIds + " );");

		int ret = 0;
		try {
			ret = queryArticlePromoPaid.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated article table for promo_id  =  " + stage + " ,promo_stage=0 (unpaid)");

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public List allArticleByPromotStage(int stage) {
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		String where = "";
		if (stage != -1) {// to get all
			where = " where promo_stage =" + stage;
//			if(stage == 0) {//0 and null both means not paid
//				where = " where promo_stage = 0 || promo_stage is null";
//			}
		}

		Query query = session.createNativeQuery(
				"SELECT article_id, title,friendly_name,window_title,ar.promo_id,pm.promo_code,promo_stage \r\n"
						+ " FROM article ar \r\n" + " inner join promo_master pm on pm.promo_id = ar.promo_id " + where
						+ ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer article_id = (Integer) objects[0];
			String title = (String) objects[1];
			String friendly_name = (String) objects[2];
			String window_title = (String) objects[3];
			Integer promo_id = (Integer) objects[4];
			String promo_code = (String) objects[5];
			Integer promo_stage = (Integer) objects[6];
			hm.put("article_id", article_id);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("window_title", window_title);
			hm.put("promo_id", promo_id);
			hm.put("promo_code", promo_code);
			hm.put("promo_stage", promo_stage);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}

}
