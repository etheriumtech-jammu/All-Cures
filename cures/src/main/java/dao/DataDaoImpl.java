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

		String device_id = (String) dataMap.get("device_id");
		
		String event_type = (String) dataMap.get("event_type");
		String event_value = (String) dataMap.get("event_value");
		int user_id = (int) dataMap.get("user_id");
//		int date = (int) dataMap.get("date");;
		

		Query query = session
				.createNativeQuery("INSERT INTO `data_store`\r\n" + " (`device_id`,"
						+ " `event_type`," + " `event_value`," + " `user_id`)" 
						+ " VALUES"
						+ " ('" + device_id + "'," + " '"+ event_type +" ' ," +"' "+event_value + "' ," + user_id + ");");
		
//		'" + promo_code + "'," + " '" + promo_start_datetime + "'
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to data_store table for device_id =  " + device_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static List viewCategories() {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("SELECT dc_id,dc_desc FROM disease_condition where dc_status=1;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
			Integer dc_id = (Integer) objects[0];
		String category = (String) objects[1];
		
		    hm.put("dc_id",dc_id);
			hm.put("category",category);
			hm.put("Image", "folder/"+ dc_id+ ".png");
			hmFinal.add(hm);
			
			
			
		}
			
		return hmFinal;

	}
	
	public static List viewMedicines() {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery("SELECT * FROM medicinetype;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
			Integer med_id = (Integer) objects[0];
		String med_type = (String) objects[1];
		
		    hm.put("med_id",med_id);
			hm.put("med_type",med_type);
			hm.put("Image", "folder/"+ med_id+ ".png");
			hmFinal.add(hm);
			
			
			
		}
			
		return hmFinal;

	}







	}
