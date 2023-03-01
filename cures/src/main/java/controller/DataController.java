package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.java_websocket.WebSocketImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Chat_Function.ClientExample;
import Chat_Function.SocketIOServer1;

import dao.DataDaoImpl;
import dao.DeleteDaoImpl;

@RestController
@RequestMapping(path = "/data")
public class DataController {

	@Autowired
	private DataDaoImpl dataDaoImpl;

	

	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addDataDetails(@RequestBody HashMap dataMasterMap) {
		return dataDaoImpl.addDataDetails(dataMasterMap);
	}
	
	
	@RequestMapping(value = "/deactivate/{usr_id}/{reason_id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int delete_update(@PathVariable Integer usr_id,@PathVariable Integer reason_id )  {
		
		System.out.println("Request to deactivate");
		return DeleteDaoImpl.Delete_Update(usr_id,reason_id);
	
		
	}	
	
	@RequestMapping(value = "/delete/{email}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody String login_delete(@PathVariable String email) {
		
		
		return DeleteDaoImpl.Login_Delete(email);		
	}
	
	@RequestMapping(value = "/reasons", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List Reasons() {
		
		return DeleteDaoImpl.Delete_Reasons();
	
		
	}
	
	@RequestMapping(value = "/startWebSocketServer", method = RequestMethod.GET)
	@ResponseBody
	public String startWebSocketServer() {
//	  try {
//	    WebSocketImpl.DEBUG = true;
	    int port = 8000; // port number
	    SocketIOServer1 s = new SocketIOServer1(port);
	      s.start();
		System.out.println("ChatServer started on port: " + s.getPort());
/*	    if (!isRunning == true) {
	    	SocketIOServer1 s = new SocketIOServer1(port);
	      s.start();
	      isRunning = true;
	   
	      System.out.println("ChatServer started on port: " + s.getPort());
	    } else {
	      System.out.println("ChatServer continues on port: " );
	    }
*/
	    new ClientExample();
	   
	  return "WebSocket server started!";
	  }
	
	
	

}
