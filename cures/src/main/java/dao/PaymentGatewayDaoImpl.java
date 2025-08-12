package dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import model.PaymentGatewayTransaction;
import service.DailyCoService;
import util.AesCryptUtil;
import util.Constant;
import util.HibernateUtil;
import util.PaymentUtil;

public class PaymentGatewayDaoImpl {

	// Synchronization lock object
	private static final Object paymentLock = new Object();

	public static HashMap<String, String> setPayment(HashMap<String, Object> appointmentMap, int appointmentID) {
		HashMap<String, String> resultMap = new HashMap<>();
		UUID uuid = UUID.randomUUID();
		String orderId = uuid.toString();

		String currency = (String) appointmentMap.get("currency");
//		String workingKey = "80923CFC322F5875BA18A25A84B3F05B";
		String workingKey = "039AE11691FCF783D1539D35C6188AF9";
		BigDecimal amount = new BigDecimal(appointmentMap.get("amount").toString());
		String redirectUrl = "https://uat.all-cures.com:444/cures/payment/ccavenue-payment-udpates";
		String cancelUrl = "https://uat.all-cures.com:444/cures/Error.jsp";
		long currentTimeMillis = new Date().getTime();
		int ccaRequestTid = (int) currentTimeMillis;
		String merchantId = "3119096";

		String ccaRequest = "ccaRequesttid=" + ccaRequestTid + "&merchant_id=" + merchantId + "&order_id=" + orderId
				+ "&currency=" + currency + "&amount=" + amount + "&redirect_url=" + redirectUrl + "&cancel_url="
				+ cancelUrl + "&language=EN";
		AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
		String encRequest = aesUtil.encrypt(ccaRequest);
		System.out.println(encRequest);
		System.out.println(orderId);
		
		int res;
		synchronized (paymentLock) { // Synchronize payment processing
			res = saveTransactionDetails(appointmentID, orderId, amount, currency);
		}
		if (res == 1) {
			// Payment was successful, add encRequest and orderId to resultMap
            resultMap.put("encRequest", encRequest);
            resultMap.put("orderID", orderId);
		} 
		return resultMap;
		
	}

	private static int saveTransactionDetails(int appointmentID, String orderID, BigDecimal amount, String currency) {

		Session session = HibernateUtil.buildSessionFactory();
	    try {
	    	
			Transaction tx = session.beginTransaction();
			PaymentGatewayTransaction payment = new PaymentGatewayTransaction();
			payment.setAppointmentId(appointmentID);
			payment.setOrderId(orderID);
			payment.setAmount(amount);
			payment.setCurrency(currency);
			session.save(payment);
			tx.commit();
			return 1; // Return 1 if insertion is successful
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
			return 0; // Return 0 if insertion fails
		}
	}

	public synchronized  static  String saveTransactionResults(HttpServletRequest request,String meeting) {
		Map<String, String> hs = PaymentUtil.decryptResponse(request);
		Session session = HibernateUtil.buildSessionFactory();
		Transaction tx = session.beginTransaction();
	    try {
	    		String orderId = hs.get("order_id"); // Get the order_id from the parameters
			
		   String transDateStr = hs.get("trans_date");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date transDate = dateFormat.parse(transDateStr);
		Double amount=Double.parseDouble(hs.get("amount"));
			Query query = session.createQuery("UPDATE PaymentGatewayTransaction "
			        + "SET order_status = '" + hs.get("order_status") + "', " 
			        + "payment_mode = '" + hs.get("payment_mode") + "', "
			        + "status_message = '" + hs.get("status_message") + "', "
			        + "bank_ref_no = '" + hs.get("bank_ref_no") + "', "
			        + "trans_date = '" + transDate + "', "
			        + "trackingID = '" + hs.get("tracking_id") + "'"
			    
			        + "WHERE order_id = '" + orderId + "'");
			System.out.println("UPDATE Payment_Gateway_Transactions "
			        + "SET order_status = '" + hs.get("order_status") + "', " 
			        + "payment_mode = '" + hs.get("payment_mode") + "', "
			        + "status_message = '" + hs.get("status_message") + "', "
			        + "bank_ref_no = '" + hs.get("bank_ref_no") + "', "
			        + "trans_date = '" + transDate + "', "
			        + "trackingID = '" + hs.get("tracking_id") + "', "
			       
			        + "WHERE order_id = '" + orderId + "'");
			
			query.executeUpdate();
			tx.commit();
		Integer docid= sendEmail(orderId,meeting);
			 updateWalletAmount(amount,docid);
			return "Success";
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
			return "Error";
		}
	}

	// Method to get order status by orderId
	public static String getOrderStatus(String orderId) {
		String orderStatus="";
		Session session = HibernateUtil.buildSessionFactory();
	    try {
	    	
	    	Query query1 = session.createNativeQuery("SELECT order_status,status_message FROM PaymentGatewayTransaction  WHERE order_id ='" + orderId + "';");
	    	List<Object[]> resultList = query1.getResultList();
			  for (Object[] result : resultList) {
		             orderStatus = (String) result[0];
		            String status = (String) result[1];
			  }
	        return orderStatus;
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the exception or handle it appropriately
	        return null;
	    }
	}

	public static Integer sendEmail(String orderID,String meeting) throws ParseException, IOException
	{
		Session session = HibernateUtil.buildSessionFactory();
		String startTime="";
		  String dateString = "";
		  Integer userID=0;
		   Integer docID =0;
		Query query1 = session.createNativeQuery("SELECT a.UserID, a.DocID, a.StartTime, a.AppointmentDate \r\n"
				+ "FROM Appointment a\r\n" 
				+ "JOIN PaymentGatewayTransaction b ON a.AppointmentID = b.AppointmentID \r\n"
				+ "  where b.order_id= '"+orderID + "';\r\n"
				+ "");
    	List<Object[]> resultList = query1.getResultList();
    	 for (Object[] result : resultList) {
             userID = (Integer) result[0];
             docID = (Integer) result[1];
              startTime=(String) result[2];
               Date appointmentDate = (Date) result[3];
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format as needed
             dateString = dateFormat.format(appointmentDate);
              
	  }
    	 
    	// Parse the appointment time
	        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
	        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a"); // 12-hour pattern with AM/PM
	        outputFormat.setDateFormatSymbols(new DateFormatSymbols(Locale.ENGLISH)); // Set symbols to English to ensure AM/PM is in English
	        
	        java.util.Date time = inputFormat.parse(startTime);
	        String formattedTime = outputFormat.format(time).toUpperCase(); // Convert AM/PM to uppercase
//	        VideoDaoImpl.sendEmail(docID, userID, meeting, dateString, formattedTime);
            
    		System.out.println("Video Link sent");
		return docID;
	}

	public static void updateWalletAmount(double amount, int docId) {
		Session session = HibernateUtil.buildSessionFactory();
		
        try {
            Transaction tx = session.beginTransaction();

            // Divide the amount based on the rule
            double wallet2Amount = amount * 0.2; // 20% to walletmasterid 2 i.e All Cures
            double wallet1Amount = amount * 0.1; // 10% to walletmasterid 1 i.e GST
            double wallet3Amount = amount * 0.7; // 70% to walletmasterid 3 if docid matches

            // Update rows in the table
            updateWalletAmount(session, 2, wallet2Amount);
            updateWalletAmount(session, 1, wallet1Amount);
            updateWalletAmount(session, 3, wallet3Amount, docId);
		System.out.println("Updated");
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateWalletAmount(Session session, int walletMasterId, double amount) {
        String hql = "UPDATE WalletHistory SET WalletAmount = WalletAmount + " + amount + " WHERE WalletMasterTypeID = " + walletMasterId + " ";
        session.createNativeQuery(hql)
                .executeUpdate();
	    System.out.println("Updated");
    }

    private static void updateWalletAmount(Session session, int walletMasterId, double amount, int docId) {
        String hql = "UPDATE WalletHistory SET WalletAmount = WalletAmount + " + amount + " WHERE WalletMasterTypeID = "+ walletMasterId + " AND OwnerID = " + docId +"";
        session.createNativeQuery(hql)
                .executeUpdate();
	    System.out.println("Updated");
    }

}
