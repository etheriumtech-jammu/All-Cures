package dao;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Locale;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import model.Appointment;
import model.AvailabilitySchedule;
import model.Doctor_New;
import model.ServicePayment;
import util.Constant;
import util.HibernateUtil;

public class AppointmentDaoImpl {
	 
	//To add a new Appointment
	public static HashMap<String, String> setAppointment(HashMap<String, Object> appointmentMap) {
		Session session = HibernateUtil.buildSessionFactory();
		 Transaction tx = session.beginTransaction();
	    try  {
	        Appointment appointment = new Appointment();
	       
	        
	        // Set appointment details
	        appointment.setDocID((Integer) appointmentMap.get("docID"));
	        appointment.setUserID((Integer) appointmentMap.get("userID"));
	        String dateString = (String) appointmentMap.get("appointmentDate");
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date parsedDate = dateFormat.parse(dateString);
	        java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
	        appointment.setAppointmentDate(sqlDate);

	        // Retrieve doctor's availability to get the slot duration
	        AvailabilitySchedule doctorAvailability = session.get(AvailabilitySchedule.class, (Integer) appointmentMap.get("docID"));
	        if (doctorAvailability != null) {
	            int slotDuration = doctorAvailability.getSlotDuration();
	            LocalTime startTime = LocalTime.parse((String) appointmentMap.get("startTime"));
	            // Calculate end time by adding start time and slot duration
	            LocalTime endTime = startTime.plusMinutes(slotDuration);

	            appointment.setStartTime(startTime.toString());
	            appointment.setEndTime(endTime.toString());
	        } else {
	            throw new Exception("Doctor availability not found for docID: " + appointmentMap.get("docID"));
	        }

	        appointment.setPaymentStatus((Integer) appointmentMap.get("paymentStatus"));
	        appointment.setStatus(2);
	        session.save(appointment);
	        tx.commit();

	        // Initiate payment process
	        HashMap<String, String> res = PaymentGatewayDaoImpl.setPayment(appointmentMap, appointment.getAppointmentID());
	        
	        	 return res; // Return encRequest if insertion is successful
	        
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the exception or handle it appropriately
	        return null; // Return 0 if insertion fails
	    }
	}



	
	//To get all the Appointments
	public static List<Appointment> getAppointments() {
		Session session = HibernateUtil.buildSessionFactory();
		Query query1 = session.createNativeQuery(
				"SELECT * FROM Appointment ;");
		List<Appointment> AppointmentList = new ArrayList<>();

		List<Object[]> resultList = query1.getResultList();
		Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

		for (Object[] obj : resultList) {
			Appointment appointment = new Appointment();
			appointment.setAppointmentID(obj[0] != null ? (Integer) obj[0] : 0);
			appointment.setDocID(obj[1] != null ? (Integer) obj[1] : 0);
			appointment.setUserID(obj[2] != null ? (Integer) obj[2] : 0);
			Date date=(Date)obj[3];
			appointment.setAppointmentDate(date);
			appointment.setStartTime(obj[4] != null ? (String) obj[4] : "");
			appointment.setEndTime(obj[5] != null ? (String) obj[5] : "");
			appointment.setRequestStatus(obj[6] != null ? (Integer) obj[6] : 0);
			appointment.setPaymentStatus(obj[7] != null ? (Integer) obj[7] : 0);
			appointment.setFailureID(obj[8] != null ? (Integer) obj[8] : 0);
			appointment.setCreatedDate((Timestamp) (obj[9] != null ? obj[9] : null));
			appointment.setLastUpdatedDate((Timestamp) (obj[10] != null ? obj[10] : null));
			appointment.setStatus(obj[11] != null ? (Integer) obj[11] : 0);
			
			AppointmentList.add(appointment);
		}

		return AppointmentList;
	}
	//To get Appointments of a particular doctor
	public static List<Appointment> getAppointmentsOfDoc(Integer docID) {
		Session session = HibernateUtil.buildSessionFactory();
		Query query1 = session.createNativeQuery(
				"SELECT * FROM Appointment where DocID=" +docID + ";");
		List<Appointment> AppointmentList = new ArrayList<>();

		List<Object[]> resultList = query1.getResultList();
		Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

		for (Object[] obj : resultList) {
			Appointment appointment = new Appointment();
			appointment.setAppointmentID(obj[0] != null ? (Integer) obj[0] : 0);
			appointment.setDocID(obj[1] != null ? (Integer) obj[1] : 0);
			appointment.setUserID(obj[2] != null ? (Integer) obj[2] : 0);
			Date date=(Date)obj[3];
			appointment.setAppointmentDate(date);
			appointment.setStartTime(obj[4] != null ? (String) obj[4] : "");
			appointment.setEndTime(obj[5] != null ? (String) obj[5] : "");
			appointment.setRequestStatus(obj[6] != null ? (Integer) obj[6] : 0);
			appointment.setPaymentStatus(obj[7] != null ? (Integer) obj[7] : 0);
			appointment.setFailureID(obj[8] != null ? (Integer) obj[8] : 0);
			appointment.setCreatedDate((Timestamp) (obj[9] != null ? obj[9] : null));
			appointment.setLastUpdatedDate((Timestamp) (obj[10] != null ? obj[10] : null));
			appointment.setStatus(obj[11] != null ? (Integer) obj[11] : 0);
			
			AppointmentList.add(appointment);
		}

		return AppointmentList;
	}

	//To get Appointments of a particular User
		public static List<Appointment> getAppointmentsOfUser(Integer docID,Integer userID) {
			Session session = HibernateUtil.buildSessionFactory();
			Query query1 = session.createNativeQuery(
					"SELECT * FROM Appointment where DocID=" +docID + " AND UserID= " + userID + ";");
			List<Appointment> AppointmentList = new ArrayList<>();

			List<Object[]> resultList = query1.getResultList();
			Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

			for (Object[] obj : resultList) {
				Appointment appointment = new Appointment();
				appointment.setAppointmentID(obj[0] != null ? (Integer) obj[0] : 0);
				appointment.setDocID(obj[1] != null ? (Integer) obj[1] : 0);
				appointment.setUserID(obj[2] != null ? (Integer) obj[2] : 0);
				Date date=(Date)obj[3];
				appointment.setAppointmentDate(date);
				appointment.setStartTime(obj[4] != null ? (String) obj[4] : "");
				appointment.setEndTime(obj[5] != null ? (String) obj[5] : "");
				appointment.setRequestStatus(obj[6] != null ? (Integer) obj[6] : 0);
				appointment.setPaymentStatus(obj[7] != null ? (Integer) obj[7] : 0);
				appointment.setFailureID(obj[8] != null ? (Integer) obj[8] : 0);
				appointment.setCreatedDate((Timestamp) (obj[9] != null ? obj[9] : null));
				appointment.setLastUpdatedDate((Timestamp) (obj[10] != null ? obj[10] : null));
				appointment.setStatus(obj[11] != null ? (Integer) obj[11] : 0);
				
				AppointmentList.add(appointment);
			}

			return AppointmentList;
		}

	//To get Appointments of a particular User
	public static List<Appointment> getAllAppointmentsOfUser(Integer userID) {
	Session session = HibernateUtil.buildSessionFactory();
	Query query1 = session.createNativeQuery(
		"SELECT \r\n"
	+ "      a.DocID, \r\n"
	+ "      a.AppointmentDate, \r\n"
	+ "      a.StartTime, \r\n"
	+ "      a.EndTime, \r\n"
	+ "      a.Status, \r\n"
	+ "      a.RequestStatus, \r\n"
	+ "      a.UserID,\r\n"
	+ "      CONCAT(d.docname_first, ' ', d.docname_middle, ' ', d.docname_last) AS DoctorName,\r\n"
	+ "      da.SlotDuration,\r\n"
	+ "      d.img_Loc,\r\n"
	+ "      m.name\r\n"
	+ "    FROM \r\n"
	+ "      Appointment a\r\n"
	+ "    LEFT JOIN \r\n"
	+ "      Doctors_New d ON a.DocID = d.docid"
	+ "    LEFT JOIN \r\n"
	+ "      DoctorAvailability da ON a.DocID = da.DocID"
	+ "    LEFT JOIN \r\n"
	+ "      medicinetype m ON m.id = d.MedicineTypeID"
	+ " where  a.UserId= " + userID + "");
	List<Appointment> AppointmentList = new ArrayList<>();
	List<Object[]> resultList = query1.getResultList();
	Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);
	for (Object[] obj : resultList) {
	Appointment appointment = new Appointment();
	appointment.setDocID(obj[0] != null ? (Integer) obj[0] : 0);
	Date date=(Date)obj[1];
	appointment.setAppointmentDate(date);
	appointment.setStartTime(obj[2] != null ? (String) obj[2] : "");
	appointment.setEndTime(obj[3] != null ? (String) obj[3] : "");
	appointment.setStatus(obj[4] != null ? (Integer) obj[4] : 0);
	appointment.setRequestStatus(obj[5] != null ? (Integer) obj[5] : 0);
	appointment.setDoctorName(obj[7] != null ? (String) obj[7] : "");
	appointment.setSlotDuration(obj[8] != null ? (Integer) obj[8] : 0);
	appointment.setImgLoc(obj[9] != null ? (String) obj[9] : "");
	appointment.setMedicineType(obj[10] != null ? (String) obj[10] : "");
	AppointmentList.add(appointment);
	}
	return AppointmentList;
	}
	//To get Total , unbooked slots and Completely Booked Dates of a particular doctor
	public static Map<String, Object> findCompletelyBookedAndAvailableDates(int doctorId) {
	    Map<String, Object> datesMap = new HashMap<>();
	    List<LocalDate> completelyBookedDates = new ArrayList<>();
	    Map<LocalDate, Set<LocalTime>> availableDates = new TreeMap<>();
	    Map<LocalDate, Set<LocalTime>> unbookedSlots = new TreeMap<>();

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();

	        // Check if the doctor exists
	        Query query = session.createNativeQuery("SELECT docid FROM Doctors_New WHERE docid = :doctorId");
	        query.setParameter("doctorId", doctorId);
	        List<Object[]> resultList = query.getResultList();

	        if (!resultList.isEmpty()) {
	            // Doctor found
	            LocalDate today = LocalDate.now();
	            LocalDate end = today.plusDays(30); // Next 30 days

	            for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
	                DayOfWeek dayOfWeek = date.getDayOfWeek();

	                if (isDoctorAvailableOnDay(session, doctorId, dayOfWeek)) {
	                    Set<LocalTime> bookedSlotsTime = getAppointmentsStartTimesForDate(doctorId, date);
	                    Set<LocalTime> slotStartTimes = calculateTotalSlots(doctorId);

	                    availableDates.put(date, slotStartTimes);

	                    // Find unbooked slots
	                    Set<LocalTime> unbookedSlotsTime = new HashSet<>(slotStartTimes);
	                    unbookedSlotsTime.removeAll(bookedSlotsTime);
	                    unbookedSlots.put(date, unbookedSlotsTime);
			    System.out.println("slotStartTimes.size()"+slotStartTimes.size());
			    System.out.println("bookedSlotsTime.size()"+bookedSlotsTime.size());
	                    if (bookedSlotsTime.size() >= slotStartTimes.size()) {
	                        completelyBookedDates.add(date);
	                    }
	                }
	            }
	        } else {
	            throw new Exception("Doctor not found with id: " + doctorId);
	        }

	        tx.commit();
	    } catch (Exception e) {
	        // Handle other exceptions
	        e.printStackTrace();
	    }

	    datesMap.put("totalDates", availableDates);
	    datesMap.put("completelyBookedDates", completelyBookedDates);
	    datesMap.put("unbookedSlots", unbookedSlots);

	    return datesMap;
	}


	private static boolean isDoctorAvailableOnDay(Session session, int doctorId, DayOfWeek dayOfWeek) {
	    String sql = "SELECT COUNT(*) " +
	                 "FROM DoctorAvailability " +
	                 "WHERE DocID = :doctorId " +
	                 "AND " + getColumnName(dayOfWeek) + " = 1";

	    Query query = session.createSQLQuery(sql)
	                        .setParameter("doctorId", doctorId);

	    BigInteger count = (BigInteger) query.uniqueResult();
	    
	    return count.intValue() > 0;
	}

	private static String getColumnName(DayOfWeek dayOfWeek) {
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
	        case SATURDAY:
	            return "WeekDayOnly";
	        case SUNDAY:
	            return "WeekDayOnly";
	            
	        default:
	            throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
	    }
	}

	private static Set<LocalTime> calculateTotalSlots(int doctorId) {
		Set<LocalTime> slotStartTimes = new TreeSet<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
	    try  {
	        // Retrieve doctor's information from the database
	        AvailabilitySchedule doctor = session.get(AvailabilitySchedule.class, doctorId);
	        
	        if (doctor != null) {
	            // Get the slot duration in minutes
	            int slotDuration = doctor.getSlotDuration() + 15;
	            
	            // Get the start and end time of the doctor's availability
	            LocalTime startTime = doctor.getFromTime().toLocalTime();
	            LocalTime endTime = doctor.getToTime().toLocalTime();
	            
	            // Calculate the total duration of availability in minutes
	            long totalMinutes = Duration.between(startTime, endTime).toMinutes();
	            
	            // Calculate the total number of slots
	            int totalSlots = (int) (totalMinutes / slotDuration);
	            
	            // Calculate the start time of each slot and add it to the map
	            for (int i = 0; i < totalSlots; i++) {
	                LocalTime slotStartTime = startTime.plusMinutes(i * (slotDuration));
	                slotStartTimes.add(slotStartTime);
	            }
	            System.out.println(slotStartTimes);
	        } else {
	            throw new Exception("Doctor not found with id: " + doctorId);
	        }
	    } catch (HibernateException e) {
	        // Handle Hibernate exceptions
	        e.printStackTrace();
	    } catch (Exception e) {
	        // Handle other exceptions
	        e.printStackTrace();
	    }

	    return slotStartTimes;
	}


	private static Set<LocalTime> getAppointmentsStartTimesForDate(int docID, LocalDate date) throws SQLException {
	    Set<LocalTime> appointmentStartTimes = new HashSet<>();
	    Session session = HibernateUtil.buildSessionFactory();
	    try {
	        Query query = session.createNativeQuery(
	            "SELECT StartTime FROM Appointment WHERE docid = " + docID + "   AND AppointmentDate = '"  + date +  "' AND status IN (1, 2)");
	        
	        List<Object> resultList = query.getResultList();
	        for (Object startTimeObj : resultList) {
	            System.out.println("Type of startTimeObj: " + startTimeObj.getClass().getName());
	        }
	        System.out.println(resultList.size());
	        for (Object startTimeObj : resultList) {
	//            if (startTimeObj instanceof Time) {
	        	String startTimeStr = (String) startTimeObj;
	            LocalTime startTime = LocalTime.parse(startTimeStr); // Assuming startTimeStr is in a valid time format
	            appointmentStartTimes.add(startTime);
	//            }
	        }
	    } catch (Exception e) {
	        // Handle exceptions
	        e.printStackTrace();
	    }
	    System.out.println("appointmentStartTimes"+appointmentStartTimes);
	    return appointmentStartTimes;
	}

	
}
