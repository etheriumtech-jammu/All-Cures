package Chat_Function;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import controller.DataController;
import dao.ChatDaoImpl;


public class SocketIOServer1 extends WebSocketServer {
	private static final AtomicInteger connectionCount = new AtomicInteger(0);
	 String roomName;
	private static final Map<String, Set<WebSocket>> rooms = new HashMap<>();
	
		
	      public SocketIOServer1(int port) {
	          super(new InetSocketAddress("all-cures.com",port));
	      }
	      
	  @Override
	  public void onOpen(WebSocket conn, ClientHandshake handshake) {
		  
	//	  roomName = handshake.getFieldValue("Room_No");
	//	  System.out.println("A client has connected" + clientId); 
		 
	    System.out.println("A client has connected");
	    conn.send("Welcome to the Chat Server");
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
		
		  if(message.equals("exit"))
		{
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
	    
	   
	    
	    
	    // Extract the recipient ID, sender ID, room name, and message from the message string
	    String sendId = message.substring(0, separatorIndex);
	    int separatorIndex1 = message.indexOf(":",separatorIndex +1);
	    int separatorIndex2 = message.indexOf(":",separatorIndex1 +1);
	    int separatorIndex3 = message.indexOf(":",separatorIndex2 +1);
	    String recipientId = message.substring(separatorIndex + 1 , separatorIndex1);
	    String roomName = message.substring(separatorIndex1 + 1 , separatorIndex2);
		Integer chat_id=    Integer.parseInt(roomName);
		Integer rec_id=Integer.parseInt(recipientId);
		Integer send_id=Integer.parseInt(sendId);
		String message1= message.substring(separatorIndex2 + 1);
		
		System.out.println(roomName);
		
		// Store the message in the database
		HashMap hm = new HashMap();
		hm.put("from_id", send_id);
		hm.put("to_id", rec_id);
		hm.put("message", message1);
		
		System.out.println("Send" +send_id);
		System.out.println("Recipient" +rec_id);
	
		

			Integer result=	ChatDaoImpl.Chat_Store(chat_id,hm);
			 // Add the client to the chat room and broadcast the message to all clients in the room
		    if (!rooms.containsKey(roomName)) {
		        rooms.put(roomName, new CopyOnWriteArraySet<>());
		    }
		    rooms.get(roomName).add(conn);

		    broadcast(roomName, message1);
		  
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
}
