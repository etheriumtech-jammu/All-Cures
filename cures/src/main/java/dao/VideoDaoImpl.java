package dao;

import java.util.Map;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Component;
import controller.UserController;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import service.SendEmailService;
import model.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import model.AvailabilitySchedule;
import model.ServiceContract;
import util.Constant;
import util.HibernateUtil;
import model.VideoFailure;
import java.io.IOException;
@Component
public class VideoDaoImpl {

	@Autowired
	private static SendEmailService emailUtil = new SendEmailService();

	public static Integer InsertSchedule( HashMap<String, Object> ScheduleMap) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  AvailabilitySchedule schedule= new AvailabilitySchedule();
		   int ServiceID=(Integer) ScheduleMap.get("ServiceID");
		  int DocID=(Integer) ScheduleMap.get("DocID");
		  Query query = session.createNativeQuery(
			        "SELECT ContractID FROM ServiceContractDetails " +
			        "WHERE ServiceID =" + ServiceID + " AND UserID = " + DocID + ";");
		Query query1=session.createNativeQuery("SELECT sc.ContractID\r\n"
		  		+ "FROM ServiceContractDetails sc\r\n"
		  		+ "INNER JOIN registration r ON sc.UserID = r.registration_id\r\n"
		  		+ "WHERE sc.ServiceID ="+ ServiceID +" AND r.DocID =" + DocID + ";");
		  		
		  Integer ContractID=0;
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
			try {
	        	 ContractID = (Integer) query1.getSingleResult();
	        	 System.out.println(ContractID);
	        	}
	        	catch (NoResultException e) {
	    			System.out.println("ContractID is null");
	    			
	    		}
	        	
	        	if(ContractID!=0)
	        	{
	        	Transaction tx = session.beginTransaction();
	        	schedule.setDocId((Integer) ScheduleMap.get("DocID"));
	        	schedule.setContractId(ContractID);
	        	schedule.setMonAvailability((Integer) ScheduleMap.get("MonAvailability"));
	        	schedule.setTueAvailability((Integer) ScheduleMap.get("TueAvailability"));
	        	schedule.setWedAvailability((Integer) ScheduleMap.get("WedAvailability"));
	        	schedule.setThuAvailability((Integer) ScheduleMap.get("ThuAvailability"));
	        	schedule.setFriAvailability((Integer) ScheduleMap.get("FriAvailability"));
	        	schedule.setWeekDayOnly((Integer) ScheduleMap.get("WeekDayOnly"));
	        	schedule.setSlotDuration((Integer) ScheduleMap.get("SlotDuration"));
	        	schedule.setFromTime((Time.valueOf((String) ScheduleMap.get("FromTime"))) );
	        	schedule.setToTime((Time.valueOf((String) ScheduleMap.get("ToTime"))) );
	        	schedule.setCreatedBy((Integer) ScheduleMap.get("CreatedBy"));
	        	schedule.setStatus((Integer) ScheduleMap.get("Status"));
	            session.save(schedule);
	            tx.commit();
	            // Return 1 if insertion is successful
	            return 1;
		}
	         else
        	{
	            	return 0;
        	}
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the exception or handle it appropriately
	            session.getTransaction().rollback();
	            // Return 0 if insertion fails
	            return 0;
	        }
	    }
	
	  public static List<AvailabilitySchedule> getSchedules() {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT\r\n"
		    		+ "    d.docname_first,\r\n"
		    		+ "    d.docname_middle,\r\n"
		    		+ "    d.docname_last,\r\n"
		    		+ "    a.DocID,\r\n"
		    		+ "    a.ContractID,\r\n"
		    		+ "    a.MonAvailability,\r\n"
		    		+ "    a.TueAvailability,\r\n"
		    		+ "    a.WedAvailability,\r\n"
		    		+ "    a.ThuAvailability,\r\n"
		    		+ "    a.FriAvailability,\r\n"
		    		+ "    a.WeekDayOnly,\r\n"
		    		+ "    a.SlotDuration,\r\n"
		    		+ "    a.FromTime,\r\n"
		    		+ "    a.ToTime,\r\n"
		    		+ "    a.CreatedDate,\r\n"
		    		+ "    a.LastUpdatedDate,\r\n"
		    		+ "    a.Status,\r\n"
		    		+ "    a.CreatedBy,\r\n"
		    		+ "    a.UpdatedBy,\r\n"
				+"	CONCAT(reg.first_name, ' ', reg.last_name) AS Created_Name,\r\n"
		    	 	+" 	CONCAT(reg1.first_name, ' ', reg1.last_name) AS Updated_Name\r\n"			     
		    		+ "FROM\r\n"
		    		+ "    DoctorAvailability a\r\n"
		    		+ "JOIN\r\n"
		    		+ "    Doctors_New d ON a.DocID = d.docid\r\n"
				+"JOIN\r\n"
		    		+" registration reg ON a.CreatedBy = reg.registration_id\r\n"
		    		+"LEFT JOIN\r\n"
		    	   	+" registration reg1 ON a.UpdatedBy = reg1.registration_id;\r\n"
		    		+ "");
		    List<AvailabilitySchedule> scheduleList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

		    for (Object[] obj : resultList) {
		    	AvailabilitySchedule schedule = new AvailabilitySchedule();

		    	schedule.setDocname_first(obj[0] != null ? (String) obj[0] : "");
		    	schedule.setDocname_middle(obj[1] != null ? (String) obj[1] : "");
		    	schedule.setDocname_middle(obj[2] != null ? (String) obj[2] : "");
		    	
		    	schedule.setDocId(obj[3] != null ? (Integer) obj[3] : 0);
		    	
		    	schedule.setContractId(obj[4] != null ? (Integer) obj[4] : 0);
		    	schedule.setMonAvailability(obj[5] != null ? (Integer) obj[5] : 0);
		    	schedule.setTueAvailability(obj[6] != null ? (Integer) obj[6] : 0);
		    	schedule.setWedAvailability(obj[7] != null ? (Integer) obj[7] : 0);
		    	schedule.setThuAvailability(obj[8] != null ? (Integer) obj[8] : 0);
		    	schedule.setFriAvailability(obj[9] != null ? (Integer) obj[9] : 0);
		    	schedule.setWeekDayOnly(obj[10] != null ? (Integer) obj[10] : 0);
		    	schedule.setSlotDuration(obj[11] != null ? (Integer) obj[11] : 0);
		    	schedule.setFromTime((Time) (obj[12] != null ? obj[12] : null));
		    	schedule.setToTime((Time) (obj[13] != null ? obj[13] : null));
		    	
		    	schedule.setCreatedDate((Timestamp) (obj[14] != null ? obj[14] : null));
		    	schedule.setLastUpdatedDate((Timestamp) (obj[15] != null ? obj[15] : null));
		    	schedule.setStatus(obj[16] != null ? (Integer) obj[16] : 0);
		    	schedule.setCreatedBy(obj[17] != null ? (Integer) obj[17] : 0);
		    	schedule.setUpdatedBy(obj[18] != null ? (Integer) obj[18] : 0);
			schedule.setCreated_Name(obj[19] != null ? (String) obj[19] : "");
		    	schedule.setUpdated_Name(obj[20] != null ? (String) obj[20] : "");
		    	scheduleList.add(schedule);
		    }

		    return scheduleList;
		}

	// Method to update a Schedule based on DocID and provided Map
	    
		  public static int updateSchedule(Integer DocID, HashMap ScheduleMap) {
			// String to store the update clauses
		        String updatestr = "";

		        // Construct the update clauses based on the keys in ContractMap
		       
			 
				if (ScheduleMap.containsKey("ContractID")) {
				    updatestr += "ContractID = " + ScheduleMap.get("ContractID") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("MonAvailability")) {
					updatestr +=" MonAvailability = " + ScheduleMap.get("MonAvailability") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("TueAvailability")) {
					updatestr += " TueAvailability = " + ScheduleMap.get("TueAvailability") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("WedAvailability")) {
					updatestr += " WedAvailability = " + ScheduleMap.get("WedAvailability") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("ThuAvailability")) {
					updatestr += " ThuAvailability = " + ScheduleMap.get("ThuAvailability") + ",\r\n";
				}
			  
			  if (ScheduleMap.containsKey("FriAvailability")) {
					updatestr += " FriAvailability = " + ScheduleMap.get("FriAvailability") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("WeekDayOnly")) {
					updatestr += " WeekDayOnly = " + ScheduleMap.get("WeekDayOnly") + ",\r\n";
				}
			  if (ScheduleMap.containsKey("SlotDuration")) {
					updatestr += " SlotDuration = " + ScheduleMap.get("SlotDuration") + ",\r\n";
				}
			 
			  if (ScheduleMap.containsKey("FromTime")) {
					updatestr += " FromTime = '" + ScheduleMap.get("FromTime") + "',\r\n";
				}
			  if (ScheduleMap.containsKey("ToTime")) {
					updatestr += " ToTime = '" + ScheduleMap.get("ToTime") + "',\r\n";
				}
			  
			  if (ScheduleMap.containsKey("UpdatedBy")) {
					updatestr += " UpdatedBy = " + ScheduleMap.get("UpdatedBy") + ",\r\n";
				}
			 
			  if (ScheduleMap.containsKey("Status")) {
					updatestr += " Status = " + ScheduleMap.get("Status") + ",\r\n";
				}
			// Remove trailing comma from the update string
		        updatestr = updatestr.replaceAll(",$", "");

		        // Create a Hibernate session
		        Session session = HibernateUtil.buildSessionFactory();

		        // Begin a transaction
		        session.beginTransaction();

		        // Create a native SQL query to update servicecontractdetails table
		        Query query = session.createNativeQuery("UPDATE DoctorAvailability SET " + updatestr + " WHERE DocID = " + DocID + ";");

		        int ret = 0;  // Variable to store the result of the update

		        try {
		            // Execute the update query
		            ret = query.executeUpdate();

		            // Log success message
		            Constant.log(" Updated DoctorAvailability table for DocID = " + DocID, 1);

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

		  public static int deleteSchedule(int DocID) {
				
				Session session = HibernateUtil.buildSessionFactory();

				// creating session object
				//Session session = factory;
				// creating transaction object
				session.beginTransaction();
				
				 String updatestr = " status = 0  ";
				 
				 System.out.println(updatestr);
					Query query = session.createNativeQuery(
							"UPDATE `DoctorAvailability`\r\n" + "SET\r\n" + updatestr + "WHERE `DocID` = " + DocID + ";");
					int ret = 0;
					try {
					ret = query.executeUpdate();
					session.getTransaction().commit();
					System.out.println("deleted entry for DocID =  " + DocID);
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return ret;
				 	 
			}

		  public static List<AvailabilitySchedule> getSchedule(int DocID) {
			    Session session = HibernateUtil.buildSessionFactory();
			    Query query1 = session.createNativeQuery("SELECT\r\n"
			            + "    d.docname_first,\r\n"
			            + "    d.docname_middle,\r\n"
			            + "    d.docname_last,\r\n"
			            + "    a.DocID,\r\n"
			            + "    a.ContractID,\r\n"
			            + "    a.MonAvailability,\r\n"
			            + "    a.TueAvailability,\r\n"
			            + "    a.WedAvailability,\r\n"
			            + "    a.ThuAvailability,\r\n"
			            + "    a.FriAvailability,\r\n"
			            + "    a.WeekDayOnly,\r\n"
			            + "    a.SlotDuration,\r\n"
			            + "    a.FromTime,\r\n"
			            + "    a.ToTime,\r\n"
			            + "    a.CreatedDate,\r\n"
			            + "    a.LastUpdatedDate,\r\n"
			            + "    a.Status,\r\n"
			            + "    a.CreatedBy,\r\n"
			            + "    a.UpdatedBy,\r\n"
			            + "    CONCAT(reg.first_name, ' ', reg.last_name) AS Created_Name,\r\n"
			            + "    CONCAT(reg1.first_name, ' ', reg1.last_name) AS Updated_Name\r\n"
			            + "FROM\r\n"
			            + "    DoctorAvailability a\r\n"
			            + "JOIN\r\n"
			            + "    doctors d ON a.DocID = d.docid\r\n"
			            + "JOIN\r\n"
			            + "    registration reg ON a.CreatedBy = reg.registration_id\r\n"
			            + "LEFT JOIN\r\n"
			            + "    registration reg1 ON a.UpdatedBy = reg1.registration_id\r\n"
			            + "WHERE a.DocID = " + DocID + ";");
		
			    List<AvailabilitySchedule> scheduleList = new ArrayList<>();
			    
			    List<Object[]> resultList = query1.getResultList();
			    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

			    for (Object[] obj : resultList) {
			    	AvailabilitySchedule schedule = new AvailabilitySchedule();

			    schedule.setDocname_first(obj[0] != null ? (String) obj[0] : "");
		    	schedule.setDocname_middle(obj[1] != null ? (String) obj[1] : "");
		    	schedule.setDocname_middle(obj[2] != null ? (String) obj[2] : "");
		    	
		    	schedule.setDocId(obj[3] != null ? (Integer) obj[3] : 0);
		    	
		    	schedule.setContractId(obj[4] != null ? (Integer) obj[4] : 0);
		    	schedule.setMonAvailability(obj[5] != null ? (Integer) obj[5] : 0);
		    	schedule.setTueAvailability(obj[6] != null ? (Integer) obj[6] : 0);
		    	schedule.setWedAvailability(obj[7] != null ? (Integer) obj[7] : 0);
		    	schedule.setThuAvailability(obj[8] != null ? (Integer) obj[8] : 0);
		    	schedule.setFriAvailability(obj[9] != null ? (Integer) obj[9] : 0);
		    	schedule.setWeekDayOnly(obj[10] != null ? (Integer) obj[10] : 0);
		    	schedule.setSlotDuration(obj[11] != null ? (Integer) obj[11] : 0);
		    	schedule.setFromTime((Time) (obj[12] != null ? obj[12] : null));
		    	schedule.setToTime((Time) (obj[13] != null ? obj[13] : null));
		    	
		    	schedule.setCreatedDate((Timestamp) (obj[14] != null ? obj[14] : null));
		    	schedule.setLastUpdatedDate((Timestamp) (obj[15] != null ? obj[15] : null));
		    	schedule.setStatus(obj[16] != null ? (Integer) obj[16] : 0);
		    	schedule.setCreatedBy(obj[17] != null ? (Integer) obj[17] : 0);
		    	schedule.setUpdatedBy(obj[18] != null ? (Integer) obj[18] : 0);
			schedule.setCreated_Name(obj[19] != null ? (String) obj[19] : "");
		    	schedule.setUpdated_Name(obj[20] != null ? (String) obj[20] : "");
		    	scheduleList.add(schedule);
			    }

			    return scheduleList;
			}
//Method to check whether the doctor is available for video Chat
	public static int getAvailability(int docID) {
			// TODO Auto-generated method stub
			Session session = HibernateUtil.buildSessionFactory();
			// Get today's date and day of the week
            LocalDate today = LocalDate.now();
           DayOfWeek dayOfWeek = today.getDayOfWeek();
  //          DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;

            String dayColumn = getAvailabilityColumn(dayOfWeek);
           
            String queryStr;

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                // Weekend logic
                queryStr = "SELECT *\r\n" +
                        "FROM DoctorAvailability\r\n" +
                        "WHERE\r\n" +
                        "      WeekDayOnly = 0\r\n" +
                        "      AND CURRENT_TIME BETWEEN FromTime AND ToTime AND DocID= " + docID + ";";
            }else {
                // Weekday logic
            	 queryStr = "SELECT *\r\n" +
                         "FROM DoctorAvailability \r\n" +
                         "WHERE " + dayColumn + "=1" +
                         " AND CURRENT_TIME BETWEEN FromTime AND ToTime AND DocID=" + docID + ";";
             }
            Query query = session.createNativeQuery(queryStr);
		  System.out.println(queryStr);
            List<Object[]> results = (List<Object[]>) query.getResultList();
            System.out.println(results.size());
            return results.size();
		}

		// Helper method to get the availability column based on the day of the week
	    private static String getAvailabilityColumn(DayOfWeek dayOfWeek) {
	        switch (dayOfWeek) {
	            case MONDAY:
	                return "MonAvailability";
	            case TUESDAY:
	                return "TueAvailability";
	            case WEDNESDAY:
	                return "WedAvailability";
	            case THURSDAY:
	                return "ThuAvailability";
	            case FRIDAY:
	                return "FriAvailability";
	            default:
	                return "WeekEnd";
	        }
	    }
		public static Integer InsertFailure( HashMap<String, Object> FailureMap) { 
			  Session session = HibernateUtil.buildSessionFactory();
			  VideoFailure failure= new VideoFailure();
			  
		        try {
		            // Assuming your HashMap has keys matching the property names in Service
		            // Adjust these names based on your actual Service class
		        	Transaction tx = session.beginTransaction();
		        	
		        	failure.setReasons((String) FailureMap.get("Reasons"));
		        	failure.setRefund((Integer) FailureMap.get("Refund"));
		        	failure.setPenalty((Integer) FailureMap.get("Penalty"));
		        	failure.setCreatedBy((Integer) FailureMap.get("CreatedBy"));
		            session.save(failure);
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
		  
		  public static List<VideoFailure> getFailures() {
			    Session session = HibernateUtil.buildSessionFactory();
			    Query query1 = session.createNativeQuery("SELECT\r\n"
			    		+ "    f.FailureID,\r\n"
			    		+ "    f.Reasons,\r\n"
			    		+ "    f.Refund,\r\n"
			    		+ "    f.Penalty,\r\n"
			    		+ "    f.CreatedBy,\r\n"
			    		+ "    f.CreatedDate,\r\n"
			    		+ "    f.LastUpdatedDate,\r\n"
			    		+ "    f.UpdatedBy,\r\n"
			    		+ "    f.Status,\r\n"
			    		+ "    CONCAT(regCreated.first_name, ' ', regCreated.last_name) AS CreatedByName,\r\n"
			    		+ "    CONCAT(regUpdated.first_name, ' ', regUpdated.last_name) AS UpdatedByName\r\n"
			    		+ "FROM\r\n"
			    		+ "    FailureMaster f\r\n"
			    		+ "JOIN\r\n"
			    		+ "    registration regCreated ON f.CreatedBy = regCreated.registration_id\r\n"
			    		+ "LEFT JOIN\r\n"
			    		+ "    registration regUpdated ON f.UpdatedBy = regUpdated.registration_id;\r\n"
			    		+ "");
			    List<VideoFailure> failureList = new ArrayList<>();
			    
			    List<Object[]> resultList = query1.getResultList();
			    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

			    for (Object[] obj : resultList) {
			    	VideoFailure failure = new VideoFailure();

			    	failure.setFailureID(obj[0] != null ? (Integer) obj[0] : 0);
			    	failure.setReasons(obj[1] != null ? (String) obj[1] : "");
			    	failure.setRefund(obj[2] != null ? (Integer) obj[2] : 0);
			    	failure.setPenalty(obj[3] != null ? (Integer) obj[3] : 0);
			    	
			    	failure.setCreatedBy(obj[4] != null ? (Integer) obj[4] : 0);
			    	failure.setCreatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
			    	failure.setLastUpdatedDate((Timestamp) (obj[6] != null ? obj[6] : null));
			    	
			    	failure.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
			    	failure.setStatus(obj[8] != null ? (Integer) obj[8] : 0);
			    	failure.setCreated_Name(obj[9] != null ? (String) obj[9] : "");
			    	failure.setUpdated_Name(obj[10] != null ? (String) obj[10] : "");
			    	failureList.add(failure);
			    }

			    return failureList;
			}
		  
		// Method to update a ServiceContract based on ContractID and provided ContractMap
		    
		  public static int updateFailure(Integer FailureID, HashMap FailureMap) {
			// String to store the update clauses
		        String updatestr = "";

		        // Construct the update clauses based on the keys in ContractMap
		       
			 
				if (FailureMap.containsKey("Reasons")) {
				    updatestr += "Reasons = '" + FailureMap.get("Reasons") + "',\r\n";
				}
			  if (FailureMap.containsKey("Refund")) {
					updatestr +=" Refund = " + FailureMap.get("Refund") + ",\r\n";
				}
			  if (FailureMap.containsKey("Penalty")) {
					updatestr += " Penalty = " + FailureMap.get("Penalty") + ",\r\n";
				}
			  
			  
			  if (FailureMap.containsKey("UpdatedBy")) {
					updatestr += " UpdatedBy = " + FailureMap.get("UpdatedBy") + ",\r\n";
				}
			 
			  if (FailureMap.containsKey("Status")) {
					updatestr += " Status = " + FailureMap.get("Status") + ",\r\n";
				}
			// Remove trailing comma from the update string
		        updatestr = updatestr.replaceAll(",$", "");

		        // Create a Hibernate session
		        Session session = HibernateUtil.buildSessionFactory();

		        // Begin a transaction
		        session.beginTransaction();

		        // Create a native SQL query to update servicecontractdetails table
		        Query query = session.createNativeQuery("UPDATE FailureMaster SET " + updatestr + " WHERE FailureID = " + FailureID + ";");

		        int ret = 0;  // Variable to store the result of the update

		        try {
		            // Execute the update query
		            ret = query.executeUpdate();

		            // Log success message
		            Constant.log(" Updated FailureMaster table for FailureID = " + FailureID, 1);

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

		  public static int deleteFailure(int FailureID) {
				
				Session session = HibernateUtil.buildSessionFactory();

				// creating session object
				//Session session = factory;
				// creating transaction object
				session.beginTransaction();
				
				 String updatestr = " status = 0  ";
				 
				 System.out.println(updatestr);
					Query query = session.createNativeQuery(
							"UPDATE `FailureMaster`\r\n" + "SET\r\n" + updatestr + "WHERE `FailureID` = " + FailureID + ";");
					int ret = 0;
					try {
					ret = query.executeUpdate();
					session.getTransaction().commit();
					System.out.println("deleted entry for FailureID =  " + FailureID);
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return ret;
				 	 
			}
		  
		  public static List<VideoFailure> getFailure(int FailureID) {
			    Session session = HibernateUtil.buildSessionFactory();
			    Query query1 = session.createNativeQuery("SELECT * FROM FailureMaster where FailureID =" + FailureID + ";");
			    List<VideoFailure> failureList = new ArrayList<>();
			    
			    List<Object[]> resultList = query1.getResultList();
			    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

			    for (Object[] obj : resultList) {
			    	VideoFailure failure = new VideoFailure();

			    	failure.setFailureID(obj[0] != null ? (Integer) obj[0] : 0);
			    	failure.setReasons(obj[1] != null ? (String) obj[1] : "");
			    	failure.setRefund(obj[2] != null ? (Integer) obj[2] : 0);
			    	failure.setPenalty(obj[3] != null ? (Integer) obj[3] : 0);
			    	
			    	failure.setCreatedBy(obj[4] != null ? (Integer) obj[4] : 0);
			    	failure.setCreatedDate((Timestamp) (obj[5] != null ? obj[5] : null));
			    	failure.setLastUpdatedDate((Timestamp) (obj[6] != null ? obj[6] : null));
			    	
			    	failure.setUpdatedBy(obj[7] != null ? (Integer) obj[7] : 0);
			    	failure.setStatus(obj[8] != null ? (Integer) obj[8] : 0);
			    	
			    	failureList.add(failure);
			    }

			    return failureList;
			}
	 public static Integer sendEmail(Integer docID, String meeting) throws IOException {
   		Session session = HibernateUtil.buildSessionFactory();
	String meeting_url = meeting.replaceFirst("https://", "");
	String fullName="";
	    	String email="";
	    	Query query = session.createNativeQuery(
	                   "SELECT prefix,docname_first,docname_middle,docname_last,email FROM Doctors_New WHERE DocID = " + docID + ";");
	              
	            List<Object[]> resultList = query.getResultList();
	            
	            if (!resultList.isEmpty()) {
	                // Assuming the result contains [prefix, docname_first, docname_middle, docname_last]
	                Object[] result = resultList.get(0);
	                
	                String prefix = result[0].toString();
	                String firstName = result[1].toString();
	                String middleName = result[2].toString();
	                String lastName = result[3].toString();
	                email= result[4].toString();
	                
	              
	                	 if (middleName.isEmpty()) {
	                         // If middleName is empty, construct fullName without it
	                         fullName = prefix + " " + firstName + " " + lastName;
	                     } else if(middleName.isEmpty() && lastName.isEmpty()) {
	                         // If middleName is not empty, include it in the fullName
	                    	 fullName = prefix + " " + firstName;
	                     }
	                     else
	                     {
	                    	 fullName = prefix + " " + firstName + " " + middleName + " " + lastName;
	                     }
	               
	            }

	            System.out.println(email);
        if (email != null) {
            String encEmail = new UserController().getEmailEncrypted(email);
            String link = "https://all-cures.com/notification/" + meeting_url;
	
		System.out.println(link);
            EmailDTO emailDTO = new EmailDTO();

            emailDTO.setTo(email);
    //        emailDTO.setFrom("All-Cures INFO");
            emailDTO.setSubject("Video Consultation - Appointment Confirmation");

            // Populate the template data
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("templatefile", "email/video_new.ftlh");
	                templateData.put("name", email);
	                templateData.put("linkmeeting", link);
			templateData.put("videoChatLink", link);
	                templateData.put("supportEmail", "info@etheriumtech.com");
	                templateData.put("doctorLastName", fullName);
		System.out.println(templateData);
            emailDTO.setEmailTemplateData(templateData);

            String returnEmail = emailUtil.shootEmail(emailDTO);
            System.out.println("Email sent");
            return 1;
        } else {
            System.out.println("Email Address Not Found");
            return 0;
        }
}
}
