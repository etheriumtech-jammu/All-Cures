package Chat_Function;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.client.WebSocketClient;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import javax.websocket.WebSocketContainer;

public class ClientExample {
  private WebSocketClient client;
  
  public ClientExample() {
	  
	  Map<String, String> headers = new HashMap<>();
	  headers.put("Room_No", "1");
    try {
  //    client = new WebSocketClient(new URI("ws://localhost:3013")) {
    	
     client = new WebSocketClient(new URI("ws://0.0.0.0:8000")) {
    		 
        @Override
        public void onOpen(ServerHandshake handshake) {
          System.out.println("Client has connected to the server!");
          try {
        	    client.send("Hello server!");
        	} catch (WebsocketNotConnectedException ex) {
        	    System.out.println("Error sending message: " + ex.getMessage());
        	}
          
         
          // Send a message to the server
         
        }
  
        @Override
        public void onMessage(String message) {
          System.out.println("Received..... message: " + message);
          
        }
  
        @Override
        public void onClose(int code, String reason, boolean remote) {
          System.out.println("Client has disconnected from the server");
         }
  
        @Override
        public void onError(Exception ex) {
          ex.printStackTrace();
        }
      };
  
      client.connect();
  
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
}
