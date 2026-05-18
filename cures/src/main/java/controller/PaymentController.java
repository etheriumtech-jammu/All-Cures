package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import dao.PaymentDaoImpl;
import dao.PaymentGatewayDaoImpl;
import dao.VideoDaoImpl;
import model.ServicePayment;
import model.VideoFailure;
import model.ServicePaymentMethod;
import service.DailyCoService;
import service.DirectPaymentService;
@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

	 @Autowired
	    private DailyCoService dailyCoService;
	@Autowired
	private DirectPaymentService paymentService;



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

	
	@RequestMapping(value = "/method/add", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddPaymentMethod(@RequestBody HashMap PaymentMethodMap,HttpServletRequest request ) throws Exception {

		return PaymentDaoImpl.InsertPaymentMethod(PaymentMethodMap);
		
	}
	
	@RequestMapping(value = "/method/get/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServicePaymentMethod> PaymentMethods(HttpServletRequest request) throws Exception {

		return PaymentDaoImpl.getPaymentMethods();
	}
	
	@RequestMapping(value = "/method/update/{ServicePaymentMethodID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updatePaymentMethod(@PathVariable(name = "ServicePaymentMethodID") Integer ServicePaymentMethodID, @RequestBody HashMap PaymentMethodMap, HttpServletRequest request) {
	
	return PaymentDaoImpl.updatePaymentMethod(ServicePaymentMethodID, PaymentMethodMap);
		
	}
	
	@RequestMapping(value = "/method/delete/{ServicePaymentMethodID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deletePaymentMethod(@PathVariable int ServicePaymentMethodID,  HttpServletRequest request) {
	
	return PaymentDaoImpl.deletePaymentMethod(ServicePaymentMethodID);
	
	}
	
	
	@RequestMapping(value = "/method/get/{ServicePaymentMethodID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<ServicePaymentMethod> getPaymentMethod(@PathVariable int ServicePaymentMethodID,HttpServletRequest request) throws Exception {

		return PaymentDaoImpl.getPaymentMethod(ServicePaymentMethodID);
	}

	@RequestMapping(value = "/ccavenue-payment-udpates", method = RequestMethod.POST)
    public String PaymentUpdates(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("Payment update received");
    	String meeting=dailyCoService.createMeeting(request,null);
    	String res= PaymentGatewayDaoImpl.saveTransactionResults(request,meeting);
    	System.out.println(meeting);
	response.sendRedirect("https://all-cures.com/paymentStatus"); 
    	return res;
    }
	
	@RequestMapping(value = "get/payment-udpates/{orderID}", method = RequestMethod.GET)
    public String getOrderStatus(HttpServletRequest request,@PathVariable String orderID ) {
    	
    	return PaymentGatewayDaoImpl.getOrderStatus(orderID);
    	
    }

	@GetMapping("/basic")
	public void basicPayment(HttpServletResponse response) throws Exception {

		Map<String, String> payment = paymentService.createPayment();

		String encRequest = payment.get("encRequest");
		String accessCode = payment.get("accessCode");

		// Print in console
		System.out.println("encRequest = " + encRequest);
		System.out.println("accessCode = " + accessCode);

		String html =
				"<html>" +
						"<body onload='document.f.submit()'>" +

						"<form name='f' method='post' " +
						"action='https://secure.ccavenue.com/transaction.do?command=initiateTransaction'>" +

						"<input type='hidden' name='encRequest' value='" + encRequest + "'/>" +

						"<input type='hidden' name='access_code' value='" + accessCode + "'/>" +

						"</form>" +

						"</body>" +
						"</html>";

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
