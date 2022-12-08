package dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import model.Article;

import util.HibernateUtil;

public class AnalyticsDao {

	public static List getDetails(String column, String order) {
		Session session = HibernateUtil.buildSessionFactory();

		Query query = null;

		if (order == null) {
			
			query = session.createNativeQuery(
					"SELECT count(id), CAST(date AS DATE) FROM article_details group by CAST(date AS DATE);");
		} else {

			if (order.equals("asc")) {

				if (column.equals("date")) {

					query = session.createNativeQuery(
							"SELECT count(id), CAST(date AS DATE) FROM article_details group by CAST(date AS DATE);");
				}

				else {
					System.out.println("hello");
					query = session.createNativeQuery(
							"SELECT count(id) as id, CAST(date AS DATE) FROM article_details  group by CAST(date AS DATE) order by  id asc;");
				}
			}

			else if (order.equals("desc")) {

				if (column.equals("date")) {
					query = session.createNativeQuery(
							"SELECT count(id), CAST(date AS DATE) FROM article_details group by CAST(date AS DATE) order by CAST(date AS DATE) desc;");
				}

				else {
					query = session.createNativeQuery(
							"SELECT count(id), CAST(date AS DATE) FROM article_details group by CAST(date AS DATE) order by count(id) desc ;");
				}
			}
		}

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			// System.out.println("hhh");
			BigInteger count1 = (BigInteger) objects[0];
			Date date = (Date) objects[1];
			hm.put("Count", count1);
			hm.put("Date", date);
			hmFinal.add(hm);
		}

		return hmFinal;

	}

	public static List Articlecount(String column, String order) {
		Session session = HibernateUtil.buildSessionFactory();
		Query query = null;

		if (order == null) {
			query = session.createNativeQuery(
					"Select count(id) as Totalcount, article_id  from article_details  group by article_id;");
		}

		else {
			if (order.equals("asc")) {

				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by Totalcount asc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by article_id asc;");
				}
			} else if (order.equals("desc")) {
				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by Totalcount desc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by article_id desc;");
				}

			}
		}

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();

			BigInteger count1 = (BigInteger) objects[0];

			Integer art_id = (Integer) objects[1];
			hm.put("Count", count1);
			hm.put("article_id", art_id);
			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List Countwhatsapp(String column, String order) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = null;
		String str = "whatsapp";

		if (order == null) {
			query = session.createNativeQuery(
					"Select count(id) as Totalcount, CAST(date AS DATE) as Date from article_details where info='"
							+ str + "' group by CAST(date AS DATE);");
		}

		else {
			if (order.equals("asc")) {

				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, CAST(date AS DATE) as Date from article_details where info='"
									+ str + "' group by CAST(date AS DATE) order by Totalcount asc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, CAST(date AS DATE) as Date from article_details where info='"
									+ str + "' group by CAST(date AS DATE) order by CAST(date AS DATE)  asc;");
				}
			} else if (order.equals("desc")) {
				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, CAST(date AS DATE) as Date from article_details where info='"
									+ str + "' group by CAST(date AS DATE) order by Totalcount desc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, CAST(date AS DATE) as Date from article_details where info='"
									+ str + "' group by CAST(date AS DATE) order by CAST(date AS DATE)  desc;");
				}

			}

		}

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();

			BigInteger count1 = (BigInteger) objects[0];
			Date date = (Date) objects[1];
			hm.put("Count", count1);
			hm.put("Date", date);
			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List mostvisited() {
		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by Totalcount desc limit 5;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			
			HashMap hm = new HashMap();

			BigInteger count1 = (BigInteger) objects[0];

			Integer art_id = (Integer) objects[1];
			hm.put("No. of hits", count1);
			hm.put("article_id", art_id);
			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List Daterange(String date1, String date2, String column, String order) {
		Session session = HibernateUtil.buildSessionFactory();
		System.out.println(date2);
		Query query = null;

		if (order == null) {
			query = session.createNativeQuery(
					"Select count(id) as Totalcount, article_id from article_details where DATE(date) between '"
							+ date1 + "' and '" + date2 + "'  group by article_id;" + "");

		} else {
			if (order.equals("asc")) {

				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id from article_details where DATE(date) between '"
									+ date1 + "' and '" + date2 + "'  group by article_id order by Totalcount asc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id from article_details where DATE(date) between '"
									+ date1 + "' and '" + date2 + "'  group by article_id order by article_id asc;");
				}

			} else if (order.equals("desc")) {
				if (column.equals("count")) {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id from article_details where DATE(date) between '"
									+ date1 + "' and '" + date2 + "'  group by article_id order by Totalcount desc;");
				}

				else {
					query = session.createNativeQuery(
							"Select count(id) as Totalcount, article_id from article_details where DATE(date) between '"
									+ date1 + "' and '" + date2 + "'  group by article_id order by article_id desc;");
				}

			}
		}

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();

			BigInteger count1 = (BigInteger) objects[0];

			Integer art_id = (Integer) objects[1];
			hm.put("No. of hits", count1);
			hm.put("article_id", art_id);
			hmFinal.add(hm);

		}

		return hmFinal;

	}
	
	public static List recordarticle() {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("Select article_id, title,author_firstname,author_lastname, first_name Published_by,published_date from DETAILS LEFT JOIN author  ON author.author_id=DETAILS.authoredby order by published_date desc;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
		Integer article_id = (Integer) objects[0];
			
			String title = (String) objects[1];
			
			String author_fname=(String ) objects[2];
			String author_lname=(String ) objects[3];
			String Published_by=(String) objects[4];
			
			Date published_date=(Date)objects[5];
			hm.put("Article id", article_id);
			hm.put("Title", title);
			hm.put("Author", author_fname + author_lname );
			hm.put("Published_by", Published_by);
			
			hm.put("Date", published_date);
			
			hmFinal.add(hm);
			
		}
			
		return hmFinal;

	}
	
	
	public static List rating1(String date1,String date2) {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("Select target_id ID, count(ratingVal) rating ,IF(target_type_id=1,\"Doctor\",\"Article\") About from doctorsrating where DATE(updated_at) between ' "	+ date1 + "' and '" + date2 + "'  group by target_id order by target_id desc;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
		Integer ID = (Integer) objects[0];
		BigInteger count=(BigInteger)objects[1];
			String About=(String)objects[2];
			
			hm.put("ID",ID);
			hm.put("Ratings", count);
			hm.put("About", About);
			
			hmFinal.add(hm);
			
		}
			
		return hmFinal;

	}
		
	public static List comment(String date1,String date2) {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("Select target_id ID, count(comments) ,IF(target_type_id=1,\"Doctor\",\"Article\") About from doctorsrating where DATE(updated_at) between ' "	+ date1 + "' and '" + date2 + "' and comments!=\"null\" group by target_id order by target_id desc;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
		Integer ID = (Integer) objects[0];
		BigInteger count=(BigInteger)objects[1];
			String About=(String)objects[2];
			
			hm.put("ID",ID);
			hm.put("Comments", count);
			hm.put("About", About);
			
			hmFinal.add(hm);
			
		}
			
		return hmFinal;

	}
	
}
