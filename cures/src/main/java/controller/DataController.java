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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;    
import Chat_Function.ClientExample;
import Chat_Function.SocketIOServer1;

import dao.DataDaoImpl;
import dao.DeleteDaoImpl;

@RestController
@RequestMapping(path = "/data")
public class DataController {
private SocketIOServer1 socketServer;
	@Autowired
	private DataDaoImpl dataDaoImpl;
	

	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addDataDetails(@RequestBody HashMap dataMasterMap) {
		return dataDaoImpl.addDataDetails(dataMasterMap);
	}
		
	@RequestMapping(value = "/deactivate", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int delete_update(@RequestBody HashMap dataMasterMap ) throws SQLException {
		System.out.println("Request to deactivate");
		return DeleteDaoImpl.Delete_Update(dataMasterMap);
			
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
	    int port = 8000; // port number
	    if (socketServer == null) { // Check if the server is already running
	        socketServer = new SocketIOServer1(port);
	        socketServer.start();
	        System.out.println("ChatServer started on port: " + port);
	        return "WebSocket server started!";
	    } else {
	        return "WebSocket server is already running!";
	    }
	}	

	@RequestMapping(value = "/stopWebSocketServer", method = RequestMethod.GET)
	@ResponseBody
	public String stopWebSocketServer() throws IOException, InterruptedException {
	    if (socketServer != null) {
	        socketServer.stop(); // Stop the server
	        System.out.println("ChatServer stopped.");
	        socketServer = null; // Set to null so it can be restarted later
	        return "WebSocket server stopped!";
	    } else {
	        return "WebSocket server is not running.";
	    }
	}

	@RequestMapping(value = "/newsletter/upload", produces = "application/json", method = RequestMethod.POST)
	public int NewsLetter_fileupload(@RequestParam("image") CommonsMultipartFile image) throws IOException {
		return DataDaoImpl.file_upload_NewsLetter(image);
		
	}
	
	@RequestMapping(value = "/newsletter/get", produces = "application/json", method = RequestMethod.GET)
	public List NewsLetter_fileGet() throws IOException {
		return DataDaoImpl.file_Get_NewsLetter();
		
	}
	
	@RequestMapping(value = "/doctor/image", produces = "application/json", method = RequestMethod.GET)
	public List doctor_image() throws IOException {
		return DataDaoImpl.doctor_image();
		
	}

	@RequestMapping(value = "/webStories/upload", produces = "application/json", method = RequestMethod.POST)
	public int webStories_fileupload(@RequestParam("image") CommonsMultipartFile image, 
			@RequestParam("webData") String webDataJson) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> webData = objectMapper.readValue(webDataJson, new TypeReference<HashMap<String, Object>>() {});
		return DataDaoImpl.file_upload_webStories(webData,image);
		
	}

	@RequestMapping(value = "/webStories/update/{webID}", produces = "application/json", method = RequestMethod.POST)
	public int webStories_fileupdate(@RequestParam(value = "image" , required = false) CommonsMultipartFile image, 
			@RequestParam("webData") String webDataJson,@PathVariable Integer webID) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> webData = objectMapper.readValue(webDataJson, new TypeReference<HashMap<String, Object>>() {});
		return DataDaoImpl.file_update_webStories(webData,image,webID);
		
	}
	@RequestMapping(value = "/webStories/get", produces = "application/json", method = RequestMethod.GET)
	public List webStories_fileGet() throws IOException {
		return DataDaoImpl.file_Get_webStories();
		
	}

	@RequestMapping(value = "/webStories/get/{webID}", produces = "application/json", method = RequestMethod.GET)
	public List webStories_Get(@PathVariable Integer webID) throws IOException {
		return DataDaoImpl.Get_webStories(webID);
		
	}
	@RequestMapping(value = "/medicines", produces = "application/json", method = RequestMethod.GET)
	public  List Medicines() {
		return DataDaoImpl.viewMedicines();
	}

	@RequestMapping(value = "/categories", produces = "application/json", method = RequestMethod.GET)
	public  List Categories() {
		return DataDaoImpl.viewCategories();
	}

	@RequestMapping(value = "/all/categories", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List AllCategories() {
		return DataDaoImpl.AllCategories();
	}

	@RequestMapping(value = "/all/trending/categories", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List TrendingAllCategories() {
		return DataDaoImpl.TrendingCategories();
	}
}
