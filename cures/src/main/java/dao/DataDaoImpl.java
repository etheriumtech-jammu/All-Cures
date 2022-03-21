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
public class DataDaoImpl {

	
	public static int addDataDetails(HashMap dataMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		int iemi = (int) dataMap.get("iemi");
		String search = null;
		int user_id = (int) dataMap.get("user_id");
//		int date = (int) dataMap.get("date");;
		

		Query query = session
				.createNativeQuery("INSERT INTO `data_store`\r\n" + " (`iemi`,"
						+ " `search`," + " `user_id`," 
						+ " VALUES" + " ('" + iemi + "'," + " '" + search + "'," 
						+ " '" + user_id + "',);" + "");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to data_store table for iemi =  " + iemi);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	
	



	}
