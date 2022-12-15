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
		Query query1=null;
		Query query2,query3,query4,query5,query6=null;

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
			hm.put("Daily_views", count1);
			hm.put("Date", date);
			query1 = session.createNativeQuery(
					"SELECT count(article_id),title from article where published_date='" + date + "';");
			List<Object[]> result = (List<Object[]>) query1.getResultList();
		//	System.out.println(result);
			for (Object[] object : result) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Daily_Published", count2);
			}
			
			query2 = session.createNativeQuery(
					"Select count(id),info FROM article_details where info=\"whatsapp\" and CAST(date AS DATE)='" + date + "';");
			List<Object[]> result2 = (List<Object[]>) query2.getResultList();
		//	System.out.println(result2);
			for (Object[] object : result2) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Whatsapp", count2);
			}
			
			query3 = session.createNativeQuery(
					"Select count(ratingVal),CAST(updated_at AS DATE) FROM doctorsrating where target_type_id=2 and CAST(updated_at AS DATE)='" + date + "';");
			List<Object[]> result3 = (List<Object[]>) query3.getResultList();
		//	System.out.println(result3);
			for (Object[] object : result3) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Ratings", count2);
			}
			
			
			query4 = session.createNativeQuery(
			"Select count(comments),CAST(updated_at AS DATE) FROM doctorsrating where target_type_id=2 and comments!=\"null\" and CAST(updated_at AS DATE)='" + date + "';");
			List<Object[]> result4 = (List<Object[]>) query4.getResultList();
		
		//	System.out.println(result4);
			for (Object[] object : result4) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Comments", count2);
			}

			query5 = session.createNativeQuery(
					"Select count(ratingVal),CAST(updated_at AS DATE) FROM doctorsrating where target_type_id=1 and CAST(updated_at AS DATE)='" + date + "';");
			List<Object[]> result5 = (List<Object[]>) query5.getResultList();
	//		System.out.println(result5);
			for (Object[] object : result5) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Ratings_Doctor", count2);
			}
			
		query6 = session.createNativeQuery(
				"Select count(comments),CAST(updated_at AS DATE) FROM doctorsrating where target_type_id=1 and comments!=\"null\" and CAST(updated_at AS DATE)='" + date + "';");
			
	
			List<Object[]> result6 = (List<Object[]>) query6.getResultList();
		
			
		for (Object[] object : result6) {
				BigInteger count2 = (BigInteger) object[0];
				hm.put("Comments_Doctor", count2);
				
				
		}


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
				"Select count(id) as Totalcount, article_id  from article_details  group by article_id order by Totalcount desc limit 10;");
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
