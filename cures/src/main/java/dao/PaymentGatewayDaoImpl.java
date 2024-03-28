package dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.PaymentGatewayTransaction;
import util.AesCryptUtil;
import util.HibernateUtil;

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
		BigDecimal amount = new BigDecimal((String) appointmentMap.get("amount"));
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

		try (Session session = HibernateUtil.buildSessionFactory();) {
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

	public static String saveTransactionResults(HttpServletRequest request) {
		String workingKey = "039AE11691FCF783D1539D35C6188AF9"; // Enter your 32 Bit Alphanumeric Working Key here
		String encResp = request.getParameter("encResp"); // Get the encrypted response from the request parameter
		AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
		String decResp = aesUtil.decrypt(encResp);
		Hashtable<String, String> hs = new Hashtable<>();
		StringTokenizer tokenizer = new StringTokenizer(decResp, "&");
		while (tokenizer.hasMoreTokens()) {
			String pair = tokenizer.nextToken();
			if (pair != null) {
				StringTokenizer strTok = new StringTokenizer(pair, "=");
				String pname = "";
				String pvalue = "";
				if (strTok.hasMoreTokens()) {
					pname = strTok.nextToken();
					if (strTok.hasMoreTokens()) {
						pvalue = strTok.nextToken();
					}
					hs.put(pname, pvalue);
				}
			}
		}

		// Access the key-value pairs in the Hashtable
		for (String key : hs.keySet()) {
			String value = hs.get(key);
			System.out.println("Parameter Name: " + key + ", Value: " + value);
		}

		try (Session session = HibernateUtil.buildSessionFactory();) {
			String orderId = hs.get("order_id"); // Get the order_id from the parameters
			Transaction tx = session.beginTransaction();
			Query query = session.createQuery("UPDATE PaymentGatewayTransaction " + "SET orderStatus = :orderStatus, "
					+ "paymentMode = :paymentMode, " + "statusMessage = :statusMessage, "
					+ "settlementFlag = :settlementFlag, " + "bankRefNo = :bankRefNo, "
					+ "transactionDate = :transactionDate, " + "trackingId = :trackingId, "
					+ "paymentMethod = :paymentMethod " + "WHERE orderId = :orderId");

			// Set parameter values
			query.setParameter("orderStatus", hs.get("order_status"));
			query.setParameter("paymentMode", hs.get("payment_mode"));
			query.setParameter("statusMessage", hs.get("status_message"));
			query.setParameter("settlementFlag", hs.get("settlement_flag"));
			query.setParameter("bankRefNo", hs.get("bank_ref_no"));
			query.setParameter("transactionDate", hs.get("trans_date"));
			query.setParameter("trackingId", hs.get("trackingID"));
			query.setParameter("paymentMethod", hs.get("payment_method"));
			query.setParameter("orderId", orderId);

			query.executeUpdate();
			tx.commit();

			return "Success";
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
			return "Error";
		}
	}

	// Method to get order status by orderId
	public static String getOrderStatus(String orderId) {
		String orderStatus="";
	    try (Session session = HibernateUtil.buildSessionFactory()) {
	    	
	    	Query query1 = session.createNativeQuery("SELECT order_status,status_message FROM Payment_Gateway_Transactions  WHERE order_id ='" + orderId + "';");
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

}
