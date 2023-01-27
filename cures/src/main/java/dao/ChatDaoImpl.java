package dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Chat;
import util.Constant;
import util.HibernateUtil;

public class ChatDaoImpl {

	// static Session session = HibernateUtil.buildSessionFactory();
	@Transactional
	public static Integer Chat_Store(Integer chat_id, HashMap<String, Object> chatMap) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();
		System.out.println(chat_id);
		System.out.println(session.isOpen());

		int ret = 0;
		String insertStr = "INSERT into dp_chat_history " + "(chat_id,";

		String insertStr_values = "(" + chat_id + ",";
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> chatDetail : chatMap.entrySet()) {

				insertStr += chatDetail.getKey() + ", ";

				if (chatDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (chatDetail.getValue()) + ", ";
				} else {
					insertStr_values += "'" + (String) (chatDetail.getValue()) + "' , ";
				}
			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			System.out.println(completInsertStr);

			Query query = session.createNativeQuery(completInsertStr);

			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return chat_id;
	}

	public static Integer ChatStore() {
		Session session = HibernateUtil.buildSessionFactory();
		// System.out.println(session.isOpen());

		Chat chat = new Chat();
		Date d1 = new Date();

		chat.setDate(d1);

		Transaction tx = session.beginTransaction();
		session.save(chat);
		tx.commit();

		// session.close();

		System.out.println(chat.getChat_id());
		return chat.getChat_id();
	}

	public static Integer DoctorLeads(Integer doc_id) {
		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		int ret = 0;
		String insertStr = "INSERT into dp_doctor_leads " + "(doc_id)" + "values(" + doc_id + ");";
		System.out.println(insertStr);
		
		Query query = session.createNativeQuery(insertStr);

		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
		session.getTransaction().commit();

		session.close();
		return 1;
	}
	
	public static List Chat_Search(Integer chat_id) {
		Session session = HibernateUtil.buildSessionFactory();
		
		Query query = session.createNativeQuery(
				"Select chat_id,from_id,to_id,message  from allcures1.dp_chat_history where chat_id=" + chat_id + " ;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			
			HashMap hm = new HashMap();

			Integer Chat_id = (Integer) objects[0];

			Integer from = (Integer) objects[1];
			Integer to = (Integer) objects[2];
			String message=(String) objects[3];
			hm.put("Chat_id", Chat_id);
			hm.put("From", from);
			hm.put("To", to);
			hm.put("Message", message);
			
			hmFinal.add(hm);

		}

		return hmFinal;

	}
	
	public static List Chat_History(Integer doc_id, String date) {
		Session session = HibernateUtil.buildSessionFactory();
	
		Query query = session.createNativeQuery(
				"Select chat_id,from_id,to_id,message  from allcures1.dp_chat_history where (from_id=" + doc_id + " or to_id=" + doc_id + ") and Date(time)='" + date + "' ;");
		
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			
			HashMap hm = new HashMap();

			Integer Chat_id = (Integer) objects[0];

			Integer from = (Integer) objects[1];
			Integer to = (Integer) objects[2];
			String message=(String) objects[3];
		//	Date date1=(Date)objects[4];
			hm.put("Chat_id", Chat_id);
			hm.put("From", from);
			hm.put("To", to);
			hm.put("Message", message);
	//		hm.put("Date", date1);
			
			hmFinal.add(hm);

		}
		System.out.println(hmFinal);
		return hmFinal;

	}
	
	public static Integer Chat_Archive() {
		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		int ret = 0;
		String Str = "INSERT into allcures1.dp_chat_archive select * from allcures1.dp_chat_history;";
		System.out.println(Str);
		
		Query query = session.createNativeQuery(Str);

		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
		session.getTransaction().commit();

		session.close();
		return 1;
	}
	
	public static Integer save_doc_path(Integer chat_id, String path) {
		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		int ret = 0;
	
		System.out.println("Path is"+ path);
		 
        path.toString();
		
		Query query = session.createNativeQuery(
				"Update allcures1.dp_chat_archive set document_path=\"  '" + path + "'  \"where chat_id=" + chat_id + " ;");

		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
		session.getTransaction().commit();

		session.close();
		
		return 1;
	}
	
}
