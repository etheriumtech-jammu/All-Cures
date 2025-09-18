package dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.PaymentUtil;
import model.PaymentGatewayTransaction;
import util.AesCryptUtil;
import util.Constant;
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
		String workingKey = "0C8A93B072D45A598061718B364E36B5";
		BigDecimal amount = new BigDecimal(appointmentMap.get("amount").toString());
		String redirectUrl = "https://all-cures.com:444/cures/payment/ccavenue-payment-udpates";
		String cancelUrl = "";
		long currentTimeMillis = new Date().getTime();
		int ccaRequestTid = (int) currentTimeMillis;
		String merchantId = "3119096";

		String ccaRequest = "ccaRequesttid=" + ccaRequestTid + "&merchant_id=" + merchantId + "&order_id=" + orderId
				+ "&currency=" + currency + "&amount=" + amount + "&redirect_url=" + redirectUrl + "&cancel_url="
				+ cancelUrl + "&language=EN";
		System.out.println(ccaRequest);
		AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
		String encRequest = aesUtil.encrypt(ccaRequest);
//		System.out.println(encRequest);
//		System.out.println(orderId);
		
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

	public synchronized static String saveTransactionResults(HttpServletRequest request, String meeting) {
	    Map<String, String> hs = PaymentUtil.decryptResponse(request);

	    Session session = HibernateUtil.buildSessionFactory();
	    Transaction tx = session.beginTransaction();

	    try {
	        // Inputs from gateway callback
	        String orderId      = hs.get("order_id");
	        String orderStatus  = hs.get("order_status");
	        String transDateStr = hs.get("trans_date");      // e.g. "dd/MM/yyyy HH:mm:ss"
	        String paymentMode  = hs.get("payment_mode");
	        String statusMsg    = hs.get("status_message");
	        String bankRefNo    = hs.get("bank_ref_no");
	        String trackingId   = hs.get("tracking_id");
	        BigDecimal amount   = new BigDecimal(hs.get("amount"));

	        // Parse trans_date -> java.util.Date (your entity uses Date)
	        Date transactionDate = null;
	        if (transDateStr != null && !transDateStr.isEmpty()) {
	            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            transactionDate = df.parse(transDateStr);
	        }

	        // 1) Load PG row by *entity field* name orderID
	        PaymentGatewayTransaction pg = session.createQuery(
	                "FROM PaymentGatewayTransaction p WHERE p.orderID = :oid",
	                PaymentGatewayTransaction.class)
	            .setParameter("oid", orderId)
	            .getSingleResult();

	        Long pgId = pg.getPaymentGatewayTransactionId(); // <- PK from your model

	        // 2) Update PG entity fields (Hibernate will flush an UPDATE)
	        pg.setOrderStatus(orderStatus);
	        pg.setPaymentMode(paymentMode);
	        pg.setStatusMessage(statusMsg);
	        pg.setBankRefNo(bankRefNo);
	        pg.setTransactionDate(transactionDate); // Date, matches your model
	        pg.setTrackingId(trackingId);
	        pg.setLastUpdatedDate(new Date());

	        // If 'pg' is managed (it is), no need to merge; commit will flush
	        tx.commit();

	        // 3) On success, post wallet split using the pgId
	        if ("Success".equalsIgnoreCase(orderStatus)) {
	            Integer docId = sendEmail(orderId, meeting);
	            updateWalletAmount(amount.doubleValue(), docId, pgId.intValue());
	            return "Success";
	        } else {
	            return "Payment not successful";
	        }

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
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
	        VideoDaoImpl.sendEmail(docID, userID, meeting, dateString, formattedTime);
            	return docID;
    	
	}

	// === Top-level method: owns the transaction ===
	public static void updateWalletAmount(double amount, int docId, int paymentGatewayTransactionId) {
	    Session session = HibernateUtil.buildSessionFactory(); // assuming this returns a Session; otherwise openSession()
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();

	        // Split
	        double wallet2Amount = amount * 0.20; // Platform / All Cures
	        double wallet1Amount = amount * 0.10; // GST
	        double wallet3Amount = amount * 0.70; // Doctor

	        // 1) Insert ledger rows (status=SUCCESS)
	        insertLeg(session, paymentGatewayTransactionId, 2, null, docId, wallet2Amount, "CREDIT", "SUCCESS", "20% platform");
	        insertLeg(session, paymentGatewayTransactionId, 1, null, docId, wallet1Amount, "CREDIT", "SUCCESS", "10% GST");
	        insertLeg(session, paymentGatewayTransactionId, 3, docId, docId, wallet3Amount, "CREDIT", "SUCCESS", "70% doctor");

	        // 2) Update balances
	        bumpWallet(session, 2, wallet2Amount, null);
	        bumpWallet(session, 1, wallet1Amount, null);
	        bumpWallet(session, 3, wallet3Amount, docId);

	        tx.commit();
	        System.out.println("Wallet + ledger updated successfully for pgId=" + paymentGatewayTransactionId);
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();   // keep atomicity
	        System.err.println("updateWalletAmount failed for pgId=" + paymentGatewayTransactionId
	                + ", docId=" + docId + ", amount=" + amount);
	        e.printStackTrace();
	    } 
	}

	// === Helper: insert a single WalletTransaction row, with logging + rethrow ===
	private static void insertLeg(Session s,
	                              int pgId,
	                              int walletMasterTypeId,
	                              Integer ownerId,
	                              Integer docId,
	                              double amount,
	                              String direction,   // "CREDIT" or "DEBIT"
	                              String status,      // e.g., "SUCCESS"
	                              String description) {
	    try {
	        Query q = s.createNativeQuery(
	            "INSERT INTO WalletTransaction " +
	            "(payment_gateway_transaction_id, wallet_master_type_id, owner_id, doc_id, " +
	            " amount, direction, status, description, created_at, updated_at) " +
	            "VALUES (:pg, :wm, :own, :doc, :amt, :dir, :st, :desc, NOW(), NOW())"
	        );
	        q.setParameter("pg", pgId);
	        q.setParameter("wm", walletMasterTypeId);
	        q.setParameter("own", ownerId);
	        q.setParameter("doc", docId);
	        q.setParameter("amt", amount);
	        q.setParameter("dir", direction);
	        q.setParameter("st", status);
	        q.setParameter("desc", description);
	        q.executeUpdate();
	    } catch (Exception e) {
	        System.err.println(
	            "insertLeg failed: pgId=" + pgId +
	            ", wmTypeId=" + walletMasterTypeId +
	            ", ownerId=" + ownerId +
	            ", docId=" + docId +
	            ", amount=" + amount +
	            ", direction=" + direction +
	            ", status=" + status
	        );
	        e.printStackTrace();
	        throw e; // propagate so outer tx rolls back
	    }
	}

	// === Helper: bump WalletHistory balance, with logging + rethrow ===
	private static void bumpWallet(Session s, int walletMasterId, double amount, Integer ownerId) {
	    try {
	        StringBuilder sql = new StringBuilder(
	            "UPDATE WalletHistory SET WalletAmount = WalletAmount + :amt WHERE WalletMasterTypeID = :wm"
	        );
	        if (ownerId != null) {
	            sql.append(" AND OwnerID = :own");
	        }

	        Query q = s.createNativeQuery(sql.toString());
	        q.setParameter("amt", amount);
	        q.setParameter("wm", walletMasterId);
	        if (ownerId != null) {
	            q.setParameter("own", ownerId);
	        }

	        int updated = q.executeUpdate();
	        if (updated == 0) {
	            // Optional: upsert behavior — create the row if it doesn't exist
	            // Uncomment if you need it:
	            // Query ins = s.createNativeQuery(
	            //     "INSERT INTO WalletHistory (WalletMasterTypeID, OwnerID, WalletAmount) VALUES (:wm, :own, :amt)"
	            // );
	            // ins.setParameter("wm", walletMasterId);
	            // ins.setParameter("own", ownerId);
	            // ins.setParameter("amt", amount);
	            // ins.executeUpdate();
	            System.err.println("bumpWallet warning: no rows updated for wm=" + walletMasterId + ", ownerId=" + ownerId);
	        }
	    } catch (Exception e) {
	        System.err.println(
	            "bumpWallet failed: wm=" + walletMasterId +
	            ", ownerId=" + ownerId +
	            ", amount=" + amount
	        );
	        e.printStackTrace();
	        throw e; // propagate so outer tx rolls back
	    }
	}

}
