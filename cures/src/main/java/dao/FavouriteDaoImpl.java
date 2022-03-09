package dao;

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

		Query query = session.createNativeQuery("SELECT `favourite`.`user_id`," + "	`favourite`.`article_id` "
				+ "FROM favourite where user_id=" + userid + " and article_id =" + articleid + ";"
				
			
				
				)
				
				;

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer user_id = (Integer) objects[0];
			Integer article_id = (Integer) objects[1];

			hm.put("user_id", user_id);
			hm.put("article_id", article_id);

			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;

	}

	public int addFavouriteDetails(int userid, int articleid) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		// Session session = factory;
		// creating transaction object
		session.beginTransaction();

		Query query = session.createNativeQuery("INSERT INTO `favourite`" + " (`user_id`, `article_id`)" + " VALUES" + " (" + userid + "," + articleid + ");");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println(
					"inserted new entry to favourite table for user_id =  " + userid + " ,article_id=" + articleid);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		}

		return ret;

	}
}
