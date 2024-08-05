package dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import javax.persistence.NoResultException;
import com.google.firebase.messaging.FirebaseMessagingException;

import Chat_Function.NotificationService;
import util.HibernateUtil;
import model.Token;
import org.hibernate.Transaction;
public class FCMDao {

	public static void Tip_Send(HashMap<String, Object> TipMap) throws FirebaseMessagingException, IOException {
		List<String> recipientTokens = getTokens();
		String title = (String) TipMap.get("title");
	   	 String body=(String) TipMap.get("body");
		String action=(String) TipMap.get("action");
		String id="";
		NotificationService.sendNotification(recipientTokens, title, body,action,id);
		System.out.println("Message Sent");
	}

	public static List getTokens() {
		Session session = HibernateUtil.buildSessionFactory();
		String res = null;
		List<String> titles = new ArrayList<>();
		Query query = session.createNativeQuery("SELECT token_name FROM tip_token");
		List<String> results = query.getResultList();

		titles.addAll(results);

		return titles;
	}
	
	public static Integer Token_Add(String token) {
		Session session = HibernateUtil.buildSessionFactory();

		 int res = 0;
		 int ret = 0;
			Query query = session.createNativeQuery(
					"Select token_id  from tip_token where token_name=" + token + " ;");

			try {
				res = (int) query.getSingleResult();
				
			} catch(NoResultException e)
			{
				System.out.println("Not existed");
			}
			if(res == 0)
			{
			session.beginTransaction();
			String insertStr = "INSERT into tip_token " + "(token_name)" + "values(" + token + ");";
			System.out.println(insertStr);
			
			Query query1 = session.createNativeQuery(insertStr);
			// needs other condition too but unable to find correct column
			ret = query1.executeUpdate();
		
			session.getTransaction().commit();
			
			}
		
			else{
				System.out.println("Already existed");
			}
		return ret;
	}

	public static Integer Token_Add(String token, Integer registration_id) {
	    Session session = HibernateUtil.buildSessionFactory();
	    Transaction transaction = null;
	    Integer ret = 0;

	    try {
	        
	        transaction = session.beginTransaction();

	        Query<Token> query = session.createQuery(
	                "FROM Token WHERE tokenName = :token", Token.class);
	        query.setParameter("token", token);

	        Token existingToken = query.uniqueResult();

	        if (existingToken != null) {
	            // Case 1: Token exists
	            if (registration_id != null && !registration_id.equals(0) ) {
	                // Update registration_id
	                existingToken.setRegistrationId(registration_id);
	                session.update(existingToken);
	                ret = 1;
	                System.out.println("Token updated with registration_id");
	            } else {
	                // registration_id not provided
	                System.out.println("Registration_id not provided. No update made.");
	            }
	        } else {
	            // Case 2 & 3: Token doesn't exist
	            Token newToken = new Token();
	            newToken.setTokenName(token);
	            if (registration_id != null ) {
	                // Case 2: registration_id provided
	                newToken.setRegistrationId(registration_id);
	                System.out.println("Token not found. Added with registration_id");
	            } else {
	                // Case 3: registration_id not provided
	                System.out.println("Token not found. Added without registration_id");
	            }
	            session.save(newToken);
	            ret = 1;
	        }

	        transaction.commit();
	    } catch (NoResultException e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } 

	    return ret;
	}
	
}
