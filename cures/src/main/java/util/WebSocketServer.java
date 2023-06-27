package util;

import java.io.IOException;

import Chat_Function.SocketIOServer1;

public class WebSocketServer {

	public static void main(String args[]) throws IOException {
		int port = 8000; // port number
	    SocketIOServer1 s = new SocketIOServer1(port);
	      s.start();
		System.out.println("ChatServer started on port: " + s.getPort());
	}
	
}
