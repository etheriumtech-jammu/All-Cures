package dao;

import java.io.IOException;
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

public class FCMDao {

	public static void Tip_Send(HashMap<String, Object> TipMap) throws FirebaseMessagingException, IOException {
		List<String> recipientTokens = getTokens();
		String title = (String) TipMap.get("title");
	   	 String body=(String) TipMap.get("body");
		NotificationService.sendNotification(recipientTokens, title, body);
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

	
}
