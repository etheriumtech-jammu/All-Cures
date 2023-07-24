package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import org.hibernate.query.Query;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.messaging.FirebaseMessagingException;

import dao.TipDaoImpl;


@RestController
@RequestMapping(path = "/tip")
public class TipController {

	@Autowired
	private TipDaoImpl tipDaoImpl;
	
	@RequestMapping(value = "/create/user_id/{user_id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addTipDetails(@PathVariable int user_id,@RequestBody HashMap promoMasterMap) throws FirebaseMessagingException, IOException{
		return tipDaoImpl.addTipDetails(user_id,promoMasterMap);
	}
	
	@RequestMapping(value = "/get", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getAllTipDetails() {
		return tipDaoImpl.getAllTipDetails();
	}
	
	@RequestMapping(value = "/updatetip/{tip_id}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateTipMaster(@PathVariable int tip_id, @RequestBody HashMap promoMasterMap) {
		return tipDaoImpl.updateTipDetails(tip_id, promoMasterMap);
	}
	
	@RequestMapping(value = "delete/{tip_id}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteTipMaster(@PathVariable int tip_id) {
		return tipDaoImpl.deleteTipId(tip_id);
	}
	
	@RequestMapping(value = "/{tip_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getTipDetailsById(@PathVariable int tip_id) {
		return tipDaoImpl.getTipDetailsById(tip_id);
	}
	


}
