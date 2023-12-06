package controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.PaymentDaoImpl;
import dao.VideoDaoImpl;
import model.ServicePayment;
import model.VideoFailure;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

	@RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddPayment(@RequestBody HashMap PaymentMap,HttpServletRequest request ) throws Exception {

		return PaymentDaoImpl.InsertPayment(PaymentMap);
		
	}
	
	@RequestMapping(value = "/get/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServicePayment> allServicePayments(HttpServletRequest request) throws Exception {

		return PaymentDaoImpl.getServicePayments();
	}
	
	@RequestMapping(value = "/update/{ServicePaymentMasterID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updatePayment(@PathVariable(name = "ServicePaymentMasterID") Integer ServicePaymentMasterID, @RequestBody HashMap PaymentMap, HttpServletRequest request) {
	
	return PaymentDaoImpl.updatePayment(ServicePaymentMasterID, PaymentMap);
		
	}
	
	@RequestMapping(value = "/delete/{ServicePaymentMasterID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deletePayment(@PathVariable int ServicePaymentMasterID,  HttpServletRequest request) {
	
	return PaymentDaoImpl.deletePayment(ServicePaymentMasterID);
	
	}
	
	@RequestMapping(value = "/get/{ServicePaymentMasterID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServicePayment> getfailure(@PathVariable int ServicePaymentMasterID,HttpServletRequest request) throws Exception {

		return PaymentDaoImpl.getServicePayment(ServicePaymentMasterID);
	}
	
}
