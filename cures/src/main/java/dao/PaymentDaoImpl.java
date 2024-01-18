package dao;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.ServicePayment;
import model.ServicePaymentMethod;
import util.Constant;
import util.HibernateUtil;

public class PaymentDaoImpl {
	public static Integer InsertPayment( HashMap<String, Object> PaymentMap) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  ServicePayment payment= new ServicePayment();
		  
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
	        	Transaction tx = session.beginTransaction();
	        	payment.setServicePaymentMasterName((String) PaymentMap.get("ServicePaymentMasterName"));
	        	payment.setServicePaymentDesc((String) PaymentMap.get("ServicePaymentDesc"));
	        	payment.setCreatedBy((Integer) PaymentMap.get("CreatedBy"));
	        	payment.setStatus((Integer) PaymentMap.get("Status"));
	            session.save(payment);
	            tx.commit();
	            // Return 1 if insertion is successful
	            return 1;
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the exception or handle it appropriately
	            session.getTransaction().rollback();
	            // Return 0 if insertion fails
	            return 0;
	        }
	    }
	
	public static List<ServicePayment> getServicePayments() {
	    Session session = HibernateUtil.buildSessionFactory();
	    Query query1 = session.createNativeQuery("SELECT\r\n"
				+ "    spm.ServicePaymentMasterID,\r\n"
				+ "    spm.ServicePaymentMasterName,\r\n"
				+ "    spm.ServicePaymentDesc,\r\n"
				+ "    spm.CreatedBy,\r\n"
				+ "    spm.CreatedDate,\r\n"
				+ "    spm.LastUpdatedDate,\r\n"
				+ "    spm.Status,\r\n"
				+ "    spm.UpdatedBy,\r\n"
				+ "    CONCAT(regCreated.first_name, ' ', regCreated.last_name) AS CreatedByName,\r\n"
				+ "    CONCAT(regUpdated.first_name, ' ', regUpdated.last_name) AS UpdatedByName\r\n"
				+ "FROM\r\n"
				+ "    ServicePaymentMaster spm\r\n"
				+ "JOIN\r\n"
				+ "    registration regCreated ON spm.CreatedBy = regCreated.registration_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "    registration regUpdated ON spm.UpdatedBy = regUpdated.registration_id;\r\n"
				+ "");
	    List<ServicePayment> ServicePaymentList = new ArrayList<>();
	    
	    List<Object[]> resultList = query1.getResultList();
	    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

	    for (Object[] obj : resultList) {
	    	ServicePayment payment = new ServicePayment();

	    	payment.setServicePaymentMasterID(obj[0] != null ? (Integer) obj[0] : 0);
	    	payment.setServicePaymentMasterName(obj[1] != null ? (String) obj[1] : "");
	    	payment.setServicePaymentDesc(obj[2] != null ? (String) obj[2] : "");
	    	
	    	
	    	payment.setCreatedBy(obj[3] != null ? (Integer) obj[3] : 0);
	    	payment.setCreatedDate((Timestamp) (obj[4] != null ? obj[4] : null));
	    	payment.setLastUpdatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
	    	payment.setStatus(obj[6] != null ? (Integer) obj[6] : 0);
	    	payment.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
	    	
	    	payment.setCreated_Name(obj[8] != null ? (String) obj[8] : "");
		payment.setUpdated_Name(obj[9] != null ? (String) obj[9] : "");
	    	ServicePaymentList.add(payment);
	    }

	    return ServicePaymentList;
	}
  
	// Method to update a ServiceContract based on ContractID and provided ContractMap
    
	  public static int updatePayment(Integer ServicePaymentMasterID, HashMap PaymentMap) {
		// String to store the update clauses
	        String updatestr = "";

	        // Construct the update clauses based on the keys in ContractMap
	       
		 
			if (PaymentMap.containsKey("ServicePaymentMasterName")) {
			    updatestr += "ServicePaymentMasterName = '" + PaymentMap.get("ServicePaymentMasterName") + "',\r\n";
			}
		  if (PaymentMap.containsKey("ServicePaymentDesc")) {
				updatestr +=" ServicePaymentDesc =' " + PaymentMap.get("ServicePaymentDesc") + "',\r\n";
			}
		  
		  if (PaymentMap.containsKey("UpdatedBy")) {
				updatestr += " UpdatedBy = " + PaymentMap.get("UpdatedBy") + ",\r\n";
			}
		 
		  if (PaymentMap.containsKey("Status")) {
				updatestr += " Status = " + PaymentMap.get("Status") + ",\r\n";
			}
		// Remove trailing comma from the update string
	        updatestr = updatestr.replaceAll(",$", "");

	        // Create a Hibernate session
	        Session session = HibernateUtil.buildSessionFactory();

	        // Begin a transaction
	        session.beginTransaction();

	        // Create a native SQL query to update servicecontractdetails table
	        Query query = session.createNativeQuery("UPDATE ServicePaymentMaster SET " + updatestr + " WHERE ServicePaymentMasterID = " + ServicePaymentMasterID + ";");

	        int ret = 0;  // Variable to store the result of the update

	        try {
	            // Execute the update query
	            ret = query.executeUpdate();

	            // Log success message
	            Constant.log(" Updated ServicePaymentMaster table for ServicePaymentMasterID = " + ServicePaymentMasterID, 1);

	            // Commit the transaction
	            session.getTransaction().commit();
	        } catch (Exception ex) {
	            // Rollback the transaction if an exception occurs
	            session.getTransaction().rollback();
	        } finally {
	            // Uncomment the line below if you want to close the session here
	            // session.close();
	        }

	        // Return the result of the update
	        return ret;
	    }
	  
	  public static int deletePayment(int ServicePaymentMasterID) {
			
			Session session = HibernateUtil.buildSessionFactory();

			// creating session object
			//Session session = factory;
			// creating transaction object
			session.beginTransaction();
			
			 String updatestr = " status = 0  ";
			 
			 System.out.println(updatestr);
				Query query = session.createNativeQuery(
						"UPDATE `ServicePaymentMaster`\r\n" + "SET\r\n" + updatestr + "WHERE `ServicePaymentMasterID` = " + ServicePaymentMasterID + ";");
				int ret = 0;
				try {
				ret = query.executeUpdate();
				session.getTransaction().commit();
				System.out.println("deleted entry for ServicePaymentMasterID =  " + ServicePaymentMasterID);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return ret;
			 	 
		}
	  
	  public static List<ServicePayment> getServicePayment(int ServicePaymentMasterID) {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT * FROM ServicePaymentMaster where ServicePaymentMasterID =" + ServicePaymentMasterID + ";");
		    List<ServicePayment> ServicePaymentList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

		    for (Object[] obj : resultList) {
		    	ServicePayment payment = new ServicePayment();

		    	payment.setServicePaymentMasterID(obj[0] != null ? (Integer) obj[0] : 0);
		    	payment.setServicePaymentMasterName(obj[1] != null ? (String) obj[1] : "");
		    	payment.setServicePaymentDesc(obj[2] != null ? (String) obj[2] : "");
		    	payment.setCreatedBy(obj[3] != null ? (Integer) obj[3] : 0);
		    	payment.setCreatedDate((Timestamp) (obj[4] != null ? obj[4] : null));
		    	payment.setLastUpdatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
		    	payment.setStatus(obj[6] != null ? (Integer) obj[6] : 0);
		    	payment.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
		    	
		    	
		    	ServicePaymentList.add(payment);
		    }

		    return ServicePaymentList;
		}

	// Method that sets the payment method for a particular service  
	  public static Integer InsertPaymentMethod( HashMap<String, Object> PaymentMethodMap) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  ServicePaymentMethod method= new ServicePaymentMethod();
		  
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
	        	Transaction tx = session.beginTransaction();
	        	method.setServiceID((Integer) PaymentMethodMap.get("ServiceID"));
	        	method.setServicePaymentMasterID((Integer) PaymentMethodMap.get("ServicePaymentMasterID"));
	        	method.setCreatedBy((Integer) PaymentMethodMap.get("CreatedBy"));
	        	method.setStatus((Integer) PaymentMethodMap.get("Status"));
	            session.save(method);
	            tx.commit();
	            // Return 1 if insertion is successful
	            return 1;
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the exception or handle it appropriately
	            session.getTransaction().rollback();
	            // Return 0 if insertion fails
	            return 0;
	        }
	    }
	
	  public static List<ServicePaymentMethod> getPaymentMethods() {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT\r\n"
				+ "    spm.ServicePaymentMethodID,\r\n"
				+ "    spm.ServiceID,\r\n"
				+ "    spm.ServicePaymentMasterID,\r\n"
				+ "    spm.CreatedBy,\r\n"
				+ "    spm.CreatedDate,\r\n"
				+ "    spm.LastUpdatedDate,\r\n"
				+ "    spm.Status,\r\n"
				+ "    spm.UpdatedBy,\r\n"
				+ "    CONCAT(reg.first_name, ' ', reg.last_name) AS Created_Name,\r\n"
				+ "    CONCAT(reg1.first_name, ' ', reg1.last_name) AS Updated_Name,\r\n"
				+ "    spmm.ServicePaymentMasterName,\r\n"
				+ "    ssm.ServiceName\r\n"
				+ "FROM\r\n"
				+ "    ServicePaymentMethod spm\r\n"
				+ "JOIN\r\n"
				+ "    registration reg ON spm.CreatedBy = reg.registration_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "	ServicePaymentMaster spmm on spm.ServicePaymentMasterID = spmm.ServicePaymentMasterID\r\n"
				+ "LEFT JOIN\r\n"
				+ "	SponsoredServicesMaster ssm on spm.ServiceID=ssm.ServiceID\r\n"
				+ "\r\n"
				+ "LEFT JOIN\r\n"
				+ "    registration reg1 ON spm.UpdatedBy = reg1.registration_id;\r\n"
				+ "");
		    List<ServicePaymentMethod> PaymentMethodList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);
			
		    for (Object[] obj : resultList) {
		    	ServicePaymentMethod method = new ServicePaymentMethod();
		    	
		    	method.setServicePaymentMethodID(obj[0] != null ? (Integer) obj[0] : 0);
			method.setServiceID(obj[1] != null ? (Integer) obj[1] : 0);
		    	method.setServicePaymentMasterID(obj[2] != null ? (Integer) obj[2] : 0);
		    	method.setCreatedBy(obj[3] != null ? (Integer) obj[3] : 0);
		    	method.setCreatedDate((Timestamp) (obj[4] != null ? obj[4] : null));
		    	method.setLastUpdatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
		    	method.setStatus(obj[6] != null ? (Integer) obj[6] : 0);
		    	method.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
		    	method.setCreated_Name(obj[8] != null ? (String) obj[8] : "");
			method.setUpdated_Name(obj[9] != null ? (String) obj[9] : "");
			method.setPaymentName(obj[10] != null ? (String) obj[10] : "");
			method.setServiceName(obj[11] != null ? (String) obj[11] : "");
			
		    	PaymentMethodList.add(method);
		    }

		    return PaymentMethodList;
		}
	  
	// Method to update a ServicePaymentMethod based on ID and provided Map
	    public static int updatePaymentMethod(Integer ServicePaymentMethodID, HashMap PaymentMethodMap) {
			// String to store the update clauses
		        String updatestr = "";

		        // Construct the update clauses based on the keys in ContractMap
		       
	if (PaymentMethodMap.containsKey("ServiceID")) {
			updatestr += "ServiceID = '" + PaymentMethodMap.get("ServiceID") + "',\r\n";
				}
	 if (PaymentMethodMap.containsKey("ServicePaymentMasterID")) {
			updatestr +=" ServicePaymentMasterID = " + PaymentMethodMap.get("ServicePaymentMasterID") + ",\r\n";
				}
			  
	  if (PaymentMethodMap.containsKey("UpdatedBy")) {
			updatestr += " UpdatedBy = " + PaymentMethodMap.get("UpdatedBy") + ",\r\n";
				}
			 
	  if (PaymentMethodMap.containsKey("Status")) {
			updatestr += " Status = " + PaymentMethodMap.get("Status") + ",\r\n";
				}
			// Remove trailing comma from the update string
		        updatestr = updatestr.replaceAll(",$", "");

		        // Create a Hibernate session
		        Session session = HibernateUtil.buildSessionFactory();

		        // Begin a transaction
		        session.beginTransaction();

		        // Create a native SQL query to update servicecontractdetails table
		        Query query = session.createNativeQuery("UPDATE ServicePaymentMethod SET " + updatestr + " WHERE ServicePaymentMethodID = " + ServicePaymentMethodID + ";");

		        int ret = 0;  // Variable to store the result of the update

		        try {
		            // Execute the update query
		            ret = query.executeUpdate();

		            // Log success message
		            Constant.log(" Updated ServicePaymentMethod table for ServicePaymentMethodID = " + ServicePaymentMethodID, 1);

		            // Commit the transaction
		            session.getTransaction().commit();
		        } catch (Exception ex) {
		            // Rollback the transaction if an exception occurs
		            session.getTransaction().rollback();
		        } finally {
		            // Uncomment the line below if you want to close the session here
		            // session.close();
		        }

		        // Return the result of the update
		        return ret;
		    }
	// Mthod to delete a payment Method for a particular service
	  public static int deletePaymentMethod(int ServicePaymentMethodID) {
				
		Session session = HibernateUtil.buildSessionFactory();

			// creating session object
			//Session session = factory;
			// creating transaction object
				session.beginTransaction();
				
				 String updatestr = " status = 0  ";
				 
				 System.out.println(updatestr);
					Query query = session.createNativeQuery(
							"UPDATE `ServicePaymentMethod`\r\n" + "SET\r\n" + updatestr + "WHERE `ServicePaymentMethodID` = " + ServicePaymentMethodID + ";");
					int ret = 0;
					try {
					ret = query.executeUpdate();
					session.getTransaction().commit();
					System.out.println("deleted entry for ServicePaymentMethodID =  " + ServicePaymentMethodID);
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return ret;
				 	 
			}
		  
		  public static List<ServicePaymentMethod> getPaymentMethod(int ServicePaymentMethodID) {
			    Session session = HibernateUtil.buildSessionFactory();
			    Query query1 = session.createNativeQuery("SELECT\r\n"
				+ "    spm.ServicePaymentMethodID,\r\n"
				+ "    spm.ServiceID,\r\n"
				+ "    spm.ServicePaymentMasterID,\r\n"
				+ "    spm.CreatedBy,\r\n"
				+ "    spm.CreatedDate,\r\n"
				+ "    spm.LastUpdatedDate,\r\n"
				+ "    spm.Status,\r\n"
				+ "    spm.UpdatedBy,\r\n"
				+ "    CONCAT(reg.first_name, ' ', reg.last_name) AS Created_Name,\r\n"
				+ "    CONCAT(reg1.first_name, ' ', reg1.last_name) AS Updated_Name,\r\n"
				+ "    spmm.ServicePaymentMasterName,\r\n"
				+ "    ssm.ServiceName\r\n"
				+ "FROM\r\n"
				+ "    ServicePaymentMethod spm\r\n"
				+ "JOIN\r\n"
				+ "    registration reg ON spm.CreatedBy = reg.registration_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "	ServicePaymentMaster spmm on spm.ServicePaymentMasterID = spmm.ServicePaymentMasterID\r\n"
				+ "LEFT JOIN\r\n"
				+ "	SponsoredServicesMaster ssm on spm.ServiceID=ssm.ServiceID\r\n"
				+ "\r\n"
				+ "LEFT JOIN\r\n"
				+ "    registration reg1 ON spm.UpdatedBy = reg1.registration_id\r\n"
				+"    where ServicePaymentMethodID =" + ServicePaymentMethodID + ";");
			    List<ServicePaymentMethod> PaymentMethodList = new ArrayList<>();
			    
			    List<Object[]> resultList = query1.getResultList();
			    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

			    for (Object[] obj : resultList) {
			    	ServicePaymentMethod method = new ServicePaymentMethod();
			    	
			    	method.setServicePaymentMethodID(obj[0] != null ? (Integer) obj[0] : 0);
			    	method.setServicePaymentMasterID(obj[1] != null ? (Integer) obj[1] : 0);
			    	method.setServiceID(obj[2] != null ? (Integer) obj[2] : 0);
			    	
			    	method.setCreatedBy(obj[3] != null ? (Integer) obj[3] : 0);
			    	method.setCreatedDate((Timestamp) (obj[4] != null ? obj[4] : null));
			    	method.setLastUpdatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
			    	method.setStatus(obj[6] != null ? (Integer) obj[6] : 0);
			    	method.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
			    	method.setCreated_Name(obj[8] != null ? (String) obj[8] : "");
				method.setUpdated_Name(obj[9] != null ? (String) obj[9] : "");
				method.setPaymentName(obj[10] != null ? (String) obj[10] : "");
				method.setServiceName(obj[11] != null ? (String) obj[11] : "");
				PaymentMethodList.add(method);
			    }

			    return PaymentMethodList;
			}
		  
		  

	
}
