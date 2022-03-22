package dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import util.HibernateUtil;

@Component
public class FavouriteDaoImpl {

	public static List getAllFavouriteDetails(int userid, int articleid) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("SELECT favourite.user_id," + "	favourite.article_id, " + "	favourite.status, "
				+ "	article.title, " + "	article.friendly_name, " + "	article.subheading, " + "article.content_type, " + "	article.keywords, "
				+ "	article.window_title, " + "	article.content_location, " + "	article.authored_by, " + "	article.published_by, "
				+ "	article.edited_by, " + "	article.copyright_id, " + "	article.disclaimer_id, " + "	article.create_date, " + "	article.published_date, "
				+ "	article.pubstatus_id, " + "	article.language_id, " + "	article.content, " + "	article.type, " + "	article.comments, "
				+ "	article.country_id, " + "	article.over_allrating, " + "	dc.dc_name, " 
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name, "
				+ " (select count(*) from article) as count , "
				+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as rowno \r\n" 
				+ " , `article`.`medicine_type` \r\n" 
				
				
				+ " FROM favourite INNER JOIN article on favourite.article_id=article.article_id"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				+ " where favourite.user_id=" + userid + " and favourite.article_id =" + articleid

		);

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer user_id = (Integer) objects[0];
			Integer article_id = (Integer) objects[1];
			Integer status = (Integer) objects[2];
			
			String title = (String) objects[3];
			
			String friendly_name = (String) objects[4];
			String subheading = (String) objects[5];
			String content_type = (String) objects[6];
			String keywords = (String) objects[7];
			String window_title = (String) objects[8];
			String content_location = (String) objects[9];
			String authored_by = (String) objects[10];
			int published_by = objects[11] != null ? (int) objects[11] : 0;
			int edited_by = (int) objects[12];
			int copyright_id = (int) objects[13];
			int disclaimer_id = (int) objects[14];
			java.sql.Date create_date = (java.sql.Date) objects[15];
			java.sql.Date published_date = (java.sql.Date) objects[16];
			int pubstatus_id = (int) objects[17];
			int language_id = (int) objects[18];
			String content = (String) objects[19];
			String type = (String) objects[20];
			String comments = (String) objects[21];
			int country_id = objects[22] != null ? (int) objects[22] : 0;
			float over_allrating = (float) (objects[23] != null ? (Float) objects[23] : 0.0);
			String dc_name = (String) objects[24];
			String authors_name = (String) objects[25];
			BigInteger count = (BigInteger) objects[26];
			int rowno = objects[27] != null ? (int) objects[27] : 0;	
			int medicine_type = objects[28] != null ? (int) objects[28] : 0;
			
			
			
			hm.put("user_id", user_id);
			hm.put("article_id", article_id);
			hm.put("status", status);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("type", type);
			hm.put("comments", comments);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("dc_name", dc_name);
			hm.put("authors_name", authors_name);
			hm.put("count", count);
			hm.put("rowno", rowno);
			hm.put("medicine_type", medicine_type);
			hm.put("status", status);
		


			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;

	}


	public static List getAllFavouriteUserDetails(int userid) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("SELECT favourite.user_id," + "	favourite.article_id, " + "	favourite.status, " 
				+ "	article.title, " + "	article.friendly_name, " + "	article.subheading, " + "article.content_type, " + "	article.keywords, "
				+ "	article.window_title, " + "	article.content_location, " + "	article.authored_by, " + "	article.published_by, "
				+ "	article.edited_by, " + "	article.copyright_id, " + "	article.disclaimer_id, " + "	article.create_date, " + "	article.published_date, "
				+ "	article.pubstatus_id, " + "	article.language_id, " + "	article.content, " + "	article.type, " + "	article.comments, "
				+ "	article.country_id, " + "	article.over_allrating, " + "	dc.dc_name, " 
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name, "
				+ " (select count(*) from article) as count , "
				+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as rowno \r\n" 
				+ " , `article`.`medicine_type` \r\n"
				
				
				+ " FROM favourite INNER JOIN article on favourite.article_id=article.article_id"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				+ " where favourite.user_id=" + userid 

		);

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer user_id = (Integer) objects[0];
			Integer article_id = (Integer) objects[1];
			Integer status = (Integer) objects[2];
			
			String title = (String) objects[3];
			
			String friendly_name = (String) objects[4];
			String subheading = (String) objects[5];
			String content_type = (String) objects[6];
			String keywords = (String) objects[7];
			String window_title = (String) objects[8];
			String content_location = (String) objects[9];
			String authored_by = (String) objects[10];
			int published_by = objects[11] != null ? (int) objects[11] : 0;
			int edited_by = (int) objects[12];
			int copyright_id = (int) objects[13];
			int disclaimer_id = (int) objects[14];
			java.sql.Date create_date = (java.sql.Date) objects[15];
			java.sql.Date published_date = (java.sql.Date) objects[16];
			int pubstatus_id = (int) objects[17];
			int language_id = (int) objects[18];
			String content = (String) objects[19];
			String type = (String) objects[20];
			String comments = (String) objects[21];
			int country_id = objects[22] != null ? (int) objects[22] : 0;
			float over_allrating = (float) (objects[23] != null ? (Float) objects[23] : 0.0);
			String dc_name = (String) objects[24];
			String authors_name = (String) objects[25];
			BigInteger count = (BigInteger) objects[26];
			int rowno = objects[27] != null ? (int) objects[27] : 0;	
			int medicine_type = objects[28] != null ? (int) objects[28] : 0;
			
			
			hm.put("user_id", user_id);
			hm.put("article_id",article_id);
			hm.put("status", status);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("type", type);
			hm.put("comments", comments);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("dc_name", dc_name);
			hm.put("authors_name", authors_name);
			hm.put("count", count);
			hm.put("rowno", rowno);
			hm.put("medicine_type", medicine_type);
		


			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;

	}
	public int addFavouriteDetails(int userid, int articleid, int status) {
		int ret = this.updateFavouriteId(userid, articleid, status);
		if(ret == 0 ) {
			// creating seession factory object
			Session session = HibernateUtil.buildSessionFactory();
			// creating session object
			// Session session = factory;
			// creating transaction object
			session.beginTransaction();
	
			Query query = session.createNativeQuery("INSERT INTO `favourite`" + " (`user_id`, `article_id`,`status`)" + " VALUES"
					+ " (" + userid + "," + articleid + "," + status + ");");
			try {
				ret = query.executeUpdate();
				session.getTransaction().commit();
				System.out.println(
						"inserted new entry to favourite table for user_id =  " + userid + " ,article_id=" + articleid + " ,status=" + status);
	
			} catch (Exception ex) {
				session.getTransaction().rollback();
			}
		}
		return ret;

	}
	public static int updateFavouriteId(int user_id,int article_id,int status) {

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
				.createNativeQuery("UPDATE favourite SET status= "+status+ " WHERE user_id = "+ user_id + " and  article_id  = " + article_id + ";");
//		where user_id=" + userid + " and favourite.article_id =" + articleid
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from favourite, where user_id = "+ user_id + " and " + article_id + " =  " + article_id );
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		}

		return ret;
	}
	
	public static int deleteFavourite(int user_id,int article_id,int status) {

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
				.createNativeQuery("DELETE FROM `favourite` WHERE (`user_id`, `article_id`,`status`) ="
						+ " (" + user_id + "," + article_id + "," + status + ");");
//		where user_id=" + userid + " and favourite.article_id =" + articleid
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from favourite, where user_id = "+ user_id + " and " + article_id + " =  " + article_id );
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		}

		return ret;
	}
	
}
