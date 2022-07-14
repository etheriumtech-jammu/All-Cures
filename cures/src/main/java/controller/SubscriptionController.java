package controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.SubscriptionDaoImpl;


@RestController
@RequestMapping(path = "/subscription")
public class SubscriptionController {

	@Autowired
	private SubscriptionDaoImpl subscriptionDaoImpl;
	
	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addSubscriptionDetails(@RequestBody HashMap promoMasterMap) {
		return subscriptionDaoImpl.addSubscriptionDetails(promoMasterMap);
	}
	
	@RequestMapping(value = "/get", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getAllSubscriptionDetails() {
		return subscriptionDaoImpl.getAllSubscriptionDetails();
	}
	
	@RequestMapping(value = "/update/{subscription_id}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateSubscriptionMaster(@PathVariable int subscription_id, @RequestBody HashMap subscriptionMasterMap) {
		return subscriptionDaoImpl.updateSubscriptionDetails(subscription_id, subscriptionMasterMap);
	}
	
	@RequestMapping(value = "delete/{subscription_id}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteSubscriptionMaster(@PathVariable int subscription_id) {
		return subscriptionDaoImpl.deleteSubscriptionId(subscription_id);
	}
	
	@RequestMapping(value = "/{subscription_id}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List getSubscriptionDetailsById(@PathVariable int subscription_id) {
		return subscriptionDaoImpl.getSubscriptionDetailsById(subscription_id);
	}

	
}