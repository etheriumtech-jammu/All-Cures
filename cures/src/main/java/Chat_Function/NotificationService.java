package Chat_Function;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

// ...

@Service
public class NotificationService {
	public static void sendNotification(List<String> recipientTokens, String title, String body, String action, String id) throws FirebaseMessagingException, IOException {
		
		
		MulticastMessage message = ( MulticastMessage.builder()).setNotification(Notification.builder().setTitle(title).setBody(body).build())
			    .addAllTokens(recipientTokens)
			    .putData("action", action)
			     .putData("id", id)
			    .build();
		  boolean isInitialized = FirebaseApp.getApps().size() > 0;
	        if(!isInitialized)
	        {
	        	 FirebaseInitializer.initialize();
	        }
	        System.out.println(isInitialized);
	        try {
	        	
			BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
			System.out.println(response.getSuccessCount() + " messages were sent successfully");
			// Iterate through the responses to get the tokens of successfully sent messages
	        List<SendResponse> responses = response.getResponses();
	        int ret=0;
	        Session session = HibernateUtil.buildSessionFactory();
			session.beginTransaction();
	        for (int i = 0; i < responses.size(); i++) {
	            SendResponse sendResponse = responses.get(i);
	            if (sendResponse.isSuccessful()) {
	                System.out.println("Successfully sent to token: " + recipientTokens.get(i));
	                Query query = session
	        				.createNativeQuery("UPDATE tip SET status = 1 WHERE token = " + recipientTokens.get(i) + ";");
	                ret = query.executeUpdate();
	    			session.getTransaction().commit();
	                   
	            } else {
	                System.out.println("Failed to send to token: " + recipientTokens.get(i));
	                System.out.println("Error: " + sendResponse.getException().getMessage());
	            }
	        }
	        }
	        catch(FirebaseMessagingException e)
	        {
	      	  e.printStackTrace();
	        }
        
    }
}
