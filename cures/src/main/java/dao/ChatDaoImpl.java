package dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.persistence.NoResultException;
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

//			session.close();

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
//		tx.commit();

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
		try {
		ret = query.executeUpdate();
		}catch (Exception e) {
			    session.getTransaction().rollback();
			    }
			
		session.getTransaction().commit();

//		session.close();
		return ret;
	}
	
	public static List Chat_Search(Integer chat_id) {
		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"SELECT chat.chat_id,chat.to_id as to_id,reg.first_name,reg.last_name, (SELECT reg.first_name FROM registration as reg where reg.registration_id=to_id) as user_first,(SELECT reg.last_name FROM registration as reg where reg.registration_id=to_id) as user_last, chat.message,chat.time,chat.from_id\r\n"
				+ "FROM registration as reg\r\n"
				+ "LEFT JOIN dp_chat_history as chat ON chat.from_id = reg.registration_id where chat.chat_id= \r\n"
				+ "" + chat_id + " ;");
		
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("Getting results:" );
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			
			Integer Chat_id = (Integer) objects[0];
			Integer To_id=(Integer) objects[1];

			String from_first = (String) objects[2];
			String from_last = (String) objects[3];
			
			String to_first = (String) objects[4];
			String to_last = (String) objects[5];
			
			String message = (String) objects[6];
			Timestamp time=(Timestamp) objects[7];
			Integer From_id=(Integer) objects[8];
			
			hm.put("Chat_id", Chat_id);
			hm.put("From", from_first + " " + from_last);
			hm.put("To", to_first + " " + to_last);
			hm.put("Message", message);
			hm.put("Time", time);
			hm.put("From_id", From_id);
			hm.put("To_id", To_id);
			
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
		String Str = "INSERT into dp_chat_archive select * from allcures1.dp_chat_history;";
		System.out.println(Str);
		
		Query query = session.createNativeQuery(Str);

		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
//		session.getTransaction().commit();

//		session.close();
		return 1;
	}
	
	public static Integer save_doc_path(Integer chat_id, String path) {
		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

		int ret = 0;
	
		System.out.println("Path is"+ path);
		 
        path.toString();
		
		Query query = session.createNativeQuery(
				"Update dp_chat_archive set document_path=\"  '" + path + "'  \"where chat_id=" + chat_id + " ;");

		// needs other condition too but unable to find correct column
		ret = query.executeUpdate();
//		session.getTransaction().commit();

//		session.close();
		
		return 1;
	}
	
	public static List ChatStored(Integer from_id, Integer to_id) {
		Session session = HibernateUtil.buildSessionFactory();
//		session.beginTransaction();
		List hmFinal = new ArrayList();

		// System.out.println(session.isOpen());
		int res = 0;
		Query query = session.createNativeQuery(
				"Select chat_id  from dp_chat where (from_id=" + from_id + " and to_id=" + to_id + ");");
		
		try {
			res = (int) query.getSingleResult();
			
			System.out.println(res);
			
		} catch (NoResultException e) {
		e.printStackTrace();
		}
		if (res == 0) {
			
			HashMap hm = new HashMap();
			hm.put("Chat_id", null);
			
			hmFinal.add(hm);
			
		}

		else {
			
			hmFinal = Chat_Search(res);
		}

//		session.getTransaction().commit();

//		session.close();

		return hmFinal;
	}
	
	public static Integer ChatStart(Integer from_id, Integer to_id) {
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
		
			System.out.println("NEW CHAT");
			Date d1 = new Date();
			int ret=0;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String Date1 = dateFormat.format(d1);
			Query query2 = session.createNativeQuery("insert into dp_chat(from_id,to_id,date) values("
					 + from_id + "," + to_id + ", '" + Date1 + "' );");
			try {
				ret = query2.executeUpdate();
				System.out.println(ret);
				session.getTransaction().commit();
			}
			catch (Exception e) {
			    session.getTransaction().rollback();
			    }
	//		session.getTransaction().commit();

	//		session.close();
			return ret;
}
	
	
	
}
