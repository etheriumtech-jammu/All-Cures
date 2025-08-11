package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import org.quartz.SchedulerException;
import model.Appointment;
import model.AvailabilitySchedule;
import model.Doctor_New;
import model.ServicePayment;
import util.Constant;
import util.HibernateUtil;
import util.SchedulerService;

public class AppointmentDaoImpl {
	 
	//To add a new Appointment
	public static HashMap<String, Object> setAppointment(HashMap<String, Object> appointmentMap) {
        HashMap<String, Object> response = new HashMap<>();
        Session session = HibernateUtil.buildSessionFactory();
        String fullName="";
        Query<Long> query = session.createQuery(
                "SELECT COUNT(a) FROM Appointment a WHERE a.userID = :userID", Long.class);
        query.setParameter("userID", (Integer) appointmentMap.get("userID"));
        Long appointmentCount = query.uniqueResult();

        Query query1 = session.createNativeQuery(
                "SELECT token_name FROM tip_token WHERE registration_id = :userID");
        query1.setParameter("userID", (Integer) appointmentMap.get("userID"));
        String tokenName =(String) query1.uniqueResult();
        
        Query query2 = session.createNativeQuery(
                "SELECT prefix, docname_first, docname_middle, docname_last FROM Doctors_New WHERE docid = :docID");
        query2.setParameter("docID", (Integer) appointmentMap.get("docID"));
        List<Object[]> results = query2.getResultList();

        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            String prefix = row[0] != null ? row[0].toString() : "";
            String firstName = row[1] != null ? row[1].toString() : "";
            String middleName = row[2] != null ? row[2].toString() : "";
            String lastName = row[3] != null ? row[3].toString() : "";

         // Build the full name with spaces only between the names, not between prefix and first name
            StringBuilder fullNameBuilder = new StringBuilder();
            fullNameBuilder.append(prefix +" " + firstName);
           
            if (!middleName.isEmpty()) {
                fullNameBuilder.append(" ").append(middleName);
            }
            if (!lastName.isEmpty()) {
                fullNameBuilder.append(" ").append(lastName);
            }
             fullName = fullNameBuilder.toString().trim();
            System.out.println(fullName);
        } else {
            System.out.println("No doctor found with the given docID.");
        }
        try {
            Appointment appointment = new Appointment();
            Transaction tx = session.beginTransaction();

            // Set appointment details
            appointment.setDocID((Integer) appointmentMap.get("docID"));
            appointment.setUserID((Integer) appointmentMap.get("userID"));
            String dateString = (String) appointmentMap.get("appointmentDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = (Date) dateFormat.parse(dateString);
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

                // Save the appointment
                appointment.setPaymentStatus((Integer) appointmentMap.get("paymentStatus"));
                appointment.setStatus(2);
                session.save(appointment);
                tx.commit();

              
                // Initiate payment process
                HashMap<String, String> paymentResponse = PaymentGatewayDaoImpl.setPayment(appointmentMap, appointment.getAppointmentID());
                response.putAll(paymentResponse);
                // Count the number of appointments scheduled by the user
              
                // Check appointment count and set the appropriate count value in the response
                if (appointmentCount < 2) {
                    response.put("Count", "0");
                } else {
                    response.put("Count", "1");
                }

               
                System.out.println(response + " res");
              
                // Check if the gap is sufficient (24 hours) before scheduling the notification
                if (isGapSufficient(startTime, sqlDate)) {
                    SchedulerService schedulerService = new SchedulerService();
                    try {
                        schedulerService.scheduleNotification(tokenName, startTime, sqlDate,fullName);
                        System.out.println("Notification scheduled successfully.");
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        System.out.println("Failed to schedule notification: " + e.getMessage());
                    }
                } else {
                    System.out.println("Gap is less than 24 hours; notification not scheduled.");
                }

                return response; // Return response with payment response and appointment count

            } else {
                throw new Exception("Doctor availability not found for docID: " + appointmentMap.get("docID"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception or handle it appropriately
            return null; // Return null if insertion fails
        }
    }

    private static boolean isGapSufficient(LocalTime appointmentStartTime, Date appointmentDate) {
        // Get the current time
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        // Convert appointmentStartTime and appointmentDate to ZonedDateTime
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointmentDate);
        calendar.set(Calendar.HOUR_OF_DAY, appointmentStartTime.getHour());
        calendar.set(Calendar.MINUTE, appointmentStartTime.getMinute());
        calendar.set(Calendar.SECOND, 0);
        ZonedDateTime appointmentDateTime = ZonedDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        // Calculate the gap between the current time and the appointment start time
        long hoursGap = java.time.Duration.between(now, appointmentDateTime).toHours();

        // Check if the gap is at least 24 hours
        return hoursGap >= 24;
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
				"SELECT r.first_name,r.last_name,AppointmentDate,StartTime,EndTime,Status  FROM allcures_schema.Appointment a Join registration r on r.registration_id=a.UserID where a.DocID=" +docID + ";");
		List<Appointment> AppointmentList = new ArrayList<>();

		List<Object[]> resultList = query1.getResultList();
		Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

		for (Object[] obj : resultList) {
			Appointment appointment = new Appointment();
			
	        // Extracting first and last names
	        String firstName = obj[0] != null ? (String) obj[0] : "";
	        String lastName = obj[1] != null ? (String) obj[1] : "";
	        
	        // Setting concatenated username
	        String username = firstName + " " + lastName;
	        appointment.setUserName(username);
			Date date=(Date)obj[2];
			appointment.setAppointmentDate(date);
			appointment.setStartTime(obj[3] != null ? (String) obj[3] : "");
			appointment.setEndTime(obj[4] != null ? (String) obj[4] : "");
			appointment.setStatus(obj[5] != null ? (Integer) obj[5] : 0);
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
	public static List<Appointment> getAllAppointmentsOfUser(Integer userID, Date currentDate) {
	Session session = HibernateUtil.buildSessionFactory();

    StringBuilder sql = new StringBuilder(
        "SELECT " +
        "a.DocID, " +
        "a.AppointmentDate, " +
        "a.StartTime, " +
        "a.EndTime, " +
        "a.Status, " +
        "a.RequestStatus, " +
        "a.UserID, " +
        "CONCAT(d.docname_first, ' ', d.docname_middle, ' ', d.docname_last) AS DoctorName, " +
        "da.SlotDuration, " +
        "d.img_Loc, " +
        "m.name " +
        "FROM Appointment a " +
        "LEFT JOIN Doctors_New d ON a.DocID = d.docid " +
        "LEFT JOIN DoctorAvailability da ON a.DocID = da.DocID " +
        "LEFT JOIN medicinetype m ON m.id = d.MedicineTypeID " +
        "WHERE a.PaymentStatus = 1 AND a.UserID = :userID"
    );

    if (currentDate != null) {
        sql.append(" AND a.AppointmentDate >= :currentDate");
    }
    Query query = session.createNativeQuery(sql.toString());
    query.setParameter("userID", userID);
    if (currentDate != null) {
        query.setParameter("currentDate", currentDate);
    }
	List<Appointment> AppointmentList = new ArrayList<>();
	List<Object[]> resultList = query.getResultList();
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
		  BigDecimal amount = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();

	        // Check if the doctor exists
	     Query query = session.createNativeQuery(
	                "SELECT sc.Fee,d.docname_first " +
	                "FROM allcures_schema.ServiceContractDetails sc " +
	                "JOIN registration r ON r.registration_id = sc.UserID " +
	                "JOIN Doctors_New d ON d.docid = r.DocID " +
	                "WHERE d.DocID = :doctorId"
	            );
	        query.setParameter("doctorId", doctorId);
	        List<Object[]> resultList = query.getResultList();

	        if (!resultList.isEmpty()) {
	            // Doctor found
			for (Object[] row : resultList) {
	    	                // Assuming the fee is the first column and doctor's name is the second column in the result set
	    	                 amount = (BigDecimal) row[0];
	                	}
	            LocalDate today = LocalDate.now();
	            LocalDate end = today.plusDays(30); // Next 30 days

	            for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
	                DayOfWeek dayOfWeek = date.getDayOfWeek();

	                if (isDoctorAvailableOnDay(session, doctorId, dayOfWeek)) {
	                    TreeSet<LocalTime> bookedSlotsTime = getAppointmentsStartTimesForDate(doctorId, date);
				
	                    TreeSet<LocalTime> slotStartTimes = calculateTotalSlots(doctorId);
				
				  if (date.equals(today)) {
	                        LocalTime now = LocalTime.now();
	                        slotStartTimes.removeIf(slot -> !slot.isAfter(now));
	                    }
	                    availableDates.put(date, slotStartTimes);

	                    // Find unbooked slots
	                    TreeSet<LocalTime> unbookedSlotsTime = new TreeSet<>(slotStartTimes);
	                    unbookedSlotsTime.removeAll(bookedSlotsTime);
	                    unbookedSlots.put(date, unbookedSlotsTime);
	//		    System.out.println("slotStartTimes.size()"+slotStartTimes.size());
	//		    System.out.println("bookedSlotsTime.size()"+bookedSlotsTime.size());
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
	    datesMap.put("amount", amount.toString());
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
