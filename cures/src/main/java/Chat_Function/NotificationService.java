package Chat_Function;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
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
	        }
	        catch(FirebaseMessagingException e)
	        {
	      	  e.printStackTrace();
	        }
        
    }

	public void sendNotification( String body, String recipientToken, String action, String id) throws IOException {
        // Create the message for a single recipient
        Message message = Message.builder()
                .setNotification(Notification.builder()
        //                .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(recipientToken) // Use setToken for a single recipient
                .putData("action", action)
                .putData("id", id)
                .build();
        System.out.println("Notification message created: " + message.toString() + "\n");
        // Check if Firebase is initialized
        boolean isInitialized = !FirebaseApp.getApps().isEmpty();
        if (!isInitialized) {
            FirebaseInitializer.initialize(); // Ensure Firebase is initialized
        }

        System.out.println("Firebase initialized: " + isInitialized);
        try {
            // Send the notification
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Message sent successfully with response ID: " + response);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
	}

	public static void sendChatNotification( String body,String title, String recipientToken) throws IOException {
		System.out.println("body"+body);
        // Create the message for a single recipient
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(recipientToken) // Use setToken for a single recipient
               
                .build();
		 Gson gson = new Gson();
        String jsonPayload = gson.toJson(message);
        System.out.println("Payload: " + jsonPayload);
        // Check if Firebase is initialized
        boolean isInitialized = !FirebaseApp.getApps().isEmpty();
        if (!isInitialized) {
            FirebaseInitializer.initialize(); // Ensure Firebase is initialized
        }

        System.out.println("Firebase initialized: " + isInitialized);
        try {
            // Send the notification
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Message sent successfully with response ID: " + response);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
	}
	
}
