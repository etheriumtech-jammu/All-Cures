package dao;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.AvailabilitySchedule;
import model.ServiceContract;
import util.Constant;
import util.HibernateUtil;

public class VideoDaoImpl {

	public static Integer InsertSchedule( HashMap<String, Object> ScheduleMap) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  AvailabilitySchedule schedule= new AvailabilitySchedule();
		  
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
	        	Transaction tx = session.beginTransaction();
	        	schedule.setDocId((Integer) ScheduleMap.get("DocID"));
	        	schedule.setContractId((Integer) ScheduleMap.get("ContractID"));
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
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the exception or handle it appropriately
	            session.getTransaction().rollback();
	            // Return 0 if insertion fails
	            return 0;
	        }
	    }
	
	  public static List<AvailabilitySchedule> getSchedules() {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT * FROM DoctorAvailability;");
		    List<AvailabilitySchedule> scheduleList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

		    for (Object[] obj : resultList) {
		    	AvailabilitySchedule schedule = new AvailabilitySchedule();

		    	schedule.setDocId(obj[0] != null ? (Integer) obj[0] : 0);
		    	schedule.setContractId(obj[1] != null ? (Integer) obj[1] : 0);
		    	schedule.setMonAvailability(obj[2] != null ? (Integer) obj[2] : 0);
		    	schedule.setTueAvailability(obj[3] != null ? (Integer) obj[3] : 0);
		    	schedule.setWedAvailability(obj[4] != null ? (Integer) obj[4] : 0);
		    	schedule.setThuAvailability(obj[5] != null ? (Integer) obj[5] : 0);
		    	schedule.setFriAvailability(obj[6] != null ? (Integer) obj[6] : 0);
		    	schedule.setWeekDayOnly(obj[7] != null ? (Integer) obj[7] : 0);
		    	schedule.setSlotDuration(obj[8] != null ? (Integer) obj[8] : 0);
		    	schedule.setFromTime((Time) (obj[9] != null ? obj[9] : null));
		    	schedule.setToTime((Time) (obj[10] != null ? obj[10] : null));
		    	
		    	schedule.setCreatedDate((Timestamp) (obj[11] != null ? obj[11] : null));
		    	schedule.setLastUpdatedDate((Timestamp) (obj[12] != null ? obj[12] : null));
		    	schedule.setStatus(obj[13] != null ? (Integer) obj[13] : 0);
		    	schedule.setCreatedBy(obj[14] != null ? (Integer) obj[14] : 0);
		    	schedule.setUpdatedBy(obj[15] != null ? (Integer) obj[15] : 0);

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
			    Query query1 = session.createNativeQuery("SELECT * FROM DoctorAvailability where DocID =" + DocID + ";");
			    		
			    List<AvailabilitySchedule> scheduleList = new ArrayList<>();
			    
			    List<Object[]> resultList = query1.getResultList();
			    Constant.log("Executed Query and Got: " + resultList.size() + " Schedule Lists back", 1);

			    for (Object[] obj : resultList) {
			    	AvailabilitySchedule schedule = new AvailabilitySchedule();

			    	schedule.setDocId(obj[0] != null ? (Integer) obj[0] : 0);
			    	schedule.setContractId(obj[1] != null ? (Integer) obj[1] : 0);
			    	schedule.setMonAvailability(obj[2] != null ? (Integer) obj[2] : 0);
			    	schedule.setTueAvailability(obj[3] != null ? (Integer) obj[3] : 0);
			    	schedule.setWedAvailability(obj[4] != null ? (Integer) obj[4] : 0);
			    	schedule.setThuAvailability(obj[5] != null ? (Integer) obj[5] : 0);
			    	schedule.setFriAvailability(obj[6] != null ? (Integer) obj[6] : 0);
			    	schedule.setWeekDayOnly(obj[7] != null ? (Integer) obj[7] : 0);
			    	schedule.setSlotDuration(obj[8] != null ? (Integer) obj[8] : 0);
			    	schedule.setFromTime((Time) (obj[9] != null ? obj[9] : null));
			    	schedule.setToTime((Time) (obj[10] != null ? obj[10] : null));
			    	
			    	schedule.setCreatedDate((Timestamp) (obj[11] != null ? obj[11] : null));
			    	schedule.setLastUpdatedDate((Timestamp) (obj[12] != null ? obj[12] : null));
			    	schedule.setStatus(obj[13] != null ? (Integer) obj[13] : 0);
			    	schedule.setCreatedBy(obj[14] != null ? (Integer) obj[14] : 0);
			    	schedule.setUpdatedBy(obj[15] != null ? (Integer) obj[15] : 0);

			    	scheduleList.add(schedule);
			    }

			    return scheduleList;
			}

}
