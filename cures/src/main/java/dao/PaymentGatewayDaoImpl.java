package dao;

import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.query.Query;
import java.math.BigInteger;
import model.AvailabilitySchedule;
import util.AesCryptUtil;
import util.HibernateUtil;

public class PaymentGatewayDaoImpl {

	
	public static String SetPayment(HashMap<String, Object> AppointmentMap) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String orderId="";
		Integer i=1;
		Query query = session.createNativeQuery("SELECT MAX(order_id) FROM orders");
		BigInteger highestOrderId = (BigInteger) query.uniqueResult();
		if(highestOrderId == null) {
		    orderId = i.toString();
		} else {
	    i = highestOrderId.intValue() + 1;
		    orderId = Integer.toString(i);
			}
		
		String currency=(String) AppointmentMap.get("currency");
		String workingKey="039AE11691FCF783D1539D35C6188AF9";
		String amount=(String) AppointmentMap.get("amount");
		String redirect_url="https://all-cures.com:444/cures/make/ccavenue-payment-udpates";
		String cancel_url="https://uat.all-cures.com:444/cures/Error.jsp";
		long currentTimeMillis = new Date().getTime();
        int ccaRequesttid = (int) currentTimeMillis;
         String merchant_id="3119096";
         
         String ccaRequest = "ccaRequesttid=" + ccaRequesttid +
        	        "&merchant_id=" + merchant_id +
        	        "&order_id=" + orderId +
        	        "&currency=" + currency +
        	        "&amount=" + amount +
        	        "&redirect_url=" + redirect_url +
        	        "&cancel_url=" + cancel_url +
        	        "&language=EN" ;
         AesCryptUtil aesUtil=new AesCryptUtil(workingKey);
    	 String encRequest = aesUtil.encrypt(ccaRequest);
         System.out.println(encRequest);
		return encRequest;
		
	}
}
