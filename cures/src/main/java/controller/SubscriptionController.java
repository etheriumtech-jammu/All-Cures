package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.razorpay.Order;
import com.razorpay.*;

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
	
	@RequestMapping(value = "/order/userid/{user_id}/subsid/{subscription_id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int addOrderDetails(@PathVariable int user_id,@PathVariable int subscription_id,@RequestBody HashMap promoMasterMap) {
		return subscriptionDaoImpl.addOrderDetails(user_id,subscription_id,promoMasterMap);
	}
	 
	@RequestMapping(value = "/updatepayment/{order_id}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateOrderMaster(@PathVariable String order_id, @RequestBody HashMap subscriptionMasterMap) {
		return subscriptionDaoImpl.updateOrderDetails(order_id, subscriptionMasterMap);
	}

	//creating order for payment
			@PostMapping("/create_order")  
		@ResponseBody
		public String createOrder(@RequestBody Map<String, Object> data) throws Exception
		{
			System.out.println(data);
			
			int amt=Integer.parseInt(data.get("amount").toString());
			
			RazorpayClient client=new RazorpayClient("rzp_test_GgDGBdRu7fT3hC", "KrDza5wbzpVN7lUAYxiz1xRf");
			
			JSONObject ob=new JSONObject();
			ob.put("amount", amt*100);
			ob.put("currency", "INR");
			ob.put("receipt", "txn_235425");
			
			//creating new order
			
			Order order = client.Orders.create(ob);
			System.out.println(order);
			
			
			return order.toString();
		}
	

}