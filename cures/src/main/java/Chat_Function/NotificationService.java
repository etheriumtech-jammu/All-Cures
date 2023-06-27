package Chat_Function;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

// ...

@Service
public class NotificationService {
	public static void sendNotification(List<String> recipientTokens, String title, String body) throws FirebaseMessagingException, IOException {
		
		
		MulticastMessage message = ( MulticastMessage.builder()).setNotification(Notification.builder().setTitle(title).setBody(body).build())
			    .addAllTokens(recipientTokens)
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
	        }
	        catch(FirebaseMessagingException e)
	        {
	      	  e.printStackTrace();
	        }
        
    }
}
