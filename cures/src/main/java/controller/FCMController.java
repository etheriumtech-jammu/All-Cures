package controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.google.firebase.messaging.FirebaseMessagingException;


import dao.FCMDao;

@RestController
@RequestMapping(path = "/notification")
public class FCMController {
	@RequestMapping(value = "/sendtip", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody void tip_send(@RequestBody (required=false)  HashMap<String,Object> tip,HttpServletRequest request) throws FirebaseMessagingException, IOException {
	
    FCMDao.Tip_Send(tip);
    
}
	
	@RequestMapping(value = "/token/{token}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer token_store(@PathVariable String token,HttpServletRequest request) throws IOException {
	
   return FCMDao.Token_Add(token);
    
}

}
