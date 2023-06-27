package Chat_Function;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import util.Constant;
import util.Encryption;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.DataController;
import dao.ChatDaoImpl;


public class SocketIOServer1 extends WebSocketServer {
	
	private static final String KEYSTORE_PASSWORD = "Password@123";
	  private static final String KEY_PASSWORD = "Password@123";
	  private static final String KEYSTORE_PATH = "/home/uat/SSL_June/all-cures.com.jks";
	  
	
	private static final AtomicInteger connectionCount = new AtomicInteger(0);
	 String roomName;
	private static final Map<String, Set<WebSocket>> rooms = new HashMap<>();
	private static final Map<WebSocket, String> clients = new HashMap<>();
	
		
	      public SocketIOServer1(int port) {
	          super(new InetSocketAddress("0.0.0.0",port));
		   SSLContext sslContext = getSSLContext();
	    setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));   
		      
	      }
	      
	  @Override
	  public void onOpen(WebSocket conn, ClientHandshake handshake) {
		  
	//	  roomName = handshake.getFieldValue("Room_No");
	//	  System.out.println("A client has connected" + clientId); 
		 
	    System.out.println("A client has connected");
	    clients.put(conn, null);
	    
		  while (!conn.isOpen()) {
		    try {
		        Thread.sleep(1000); // Wait for 1 second
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}

	    connectionCount.incrementAndGet();
	    
	    
	  }

	  @Override
	  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	    System.out.println("A client has disconnected");
		connectionCount.decrementAndGet();
	    System.out.println(connectionCount);
	  System.out.println(connectionCount);
	    String roomName = clients.get(conn);
        if (roomName != null) {
            rooms.get(roomName).remove(conn);
        }
        clients.remove(conn);
	/*
	    if (connectionCount.decrementAndGet() == 0) {
            // All clients have disconnected, so stop the server
            try {
            	System.out.println("Server is stopping...");
            	this.stop();
            	 DataController.isRunning = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
	 
      }
 */
            // You can stop the server using whatever mechanism you prefer
            // For example, if you're using an embedded Jetty server, you can call stop() on the server object.
        }
	  

	  @Override
	  public void onMessage(WebSocket conn, String message) {
		  System.out.println("Received message is" + message);

		if (message.contains("Room_No") == true) {
			JSONObject json = new JSONObject(message);
			String roomName = json.getString("Room_No");
			System.out.println("Chat_Room is " + roomName);
			 // Add the client to the chat room
			if (!rooms.containsKey(roomName)) {
				rooms.put(roomName, new CopyOnWriteArraySet<>());
			}
			rooms.get(roomName).add(conn);
			clients.put(conn, roomName);

		}

		else {
			WebSocket sender = conn;

			if (message.equals("exit")) {
				try {
				    this.stop();
				} catch (IOException e) {
				    // Handle the exception here
					e.printStackTrace();
				}
			  	catch (InterruptedException e) {
    				// Handle the InterruptedException here
				e.printStackTrace();	
			}
			}

			// Find the separator index of the recipient ID
			int separatorIndex = message.indexOf(":");
			// Extract the recipient ID, sender ID, room name, and message from the message
			// string
			String sendId = message.substring(0, separatorIndex);
			int separatorIndex1 = message.indexOf(":", separatorIndex + 1);
			int separatorIndex2 = message.indexOf(":", separatorIndex1 + 1);
			int separatorIndex3 = message.indexOf(":", separatorIndex2 + 1);
			String recipientId = message.substring(separatorIndex + 1, separatorIndex1);
			String roomName = message.substring(separatorIndex1 + 1, separatorIndex2);
			Integer chat_id = Integer.parseInt(roomName);
			String message1 = message.substring(separatorIndex2 + 1);
			String enmsg = null;
			final String secretKey = Constant.SECRETE;
			Encryption encrypt = new Encryption();

			enmsg = encrypt.encrypt(message1, secretKey);
			System.out.println(roomName);

			System.out.println("Encrypted Message:" + enmsg);

			// Store the message in the database
			HashMap hm = new HashMap();
			hm.put("From_id", sendId);
			hm.put("To_id", recipientId);
			hm.put("Message", enmsg);

			System.out.println("Send_ID" + sendId);
			System.out.println("Recipient_ID" + recipientId);

			Integer result = ChatDaoImpl.Chat_Store(chat_id, hm);
			// broadcast the message to all clients in the room

			// broadcast(roomName, message1);
			if (rooms.containsKey(roomName)) {
				for (WebSocket client : rooms.get(roomName)) {
					if (client != sender) {
						client.send(message);
					}
				}
			}

		}
		    
		  
	 }

	  @Override
	  public void onError(WebSocket conn, Exception ex) {
	    ex.printStackTrace();
	  }
	  
	  private void broadcast(String roomName, String message) {
		    if (!rooms.containsKey(roomName)) {
		        return;
		    }
		    for (WebSocket client : rooms.get(roomName)) {
		        client.send(message);
		    }
		}

  



	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	System.out.println("Server Started");	
	}
	
	private SSLContext getSSLContext() {
        try {
          KeyStore keyStore = KeyStore.getInstance("JKS");
          keyStore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());

          KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
          kmf.init(keyStore, KEY_PASSWORD.toCharArray());

          TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
          tmf.init(keyStore);

          SSLContext sslContext = SSLContext.getInstance("TLS");
          sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
          return sslContext;
        } catch (Exception e) {
          throw new RuntimeException("Failed to initialize SSLContext", e);
        }
      }
}
