
package dao;
import java.math.BigDecimal;
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
import service.DailyCoService;
import service.FeeCalculatorService;
import org.springframework.stereotype.Component;
@Component
public class AppointmentDaoImpl {

	private static FeeCalculatorService feeCalculatorService;
    // constructor injection (preferred)
    @Autowired
    public AppointmentDaoImpl(FeeCalculatorService feeCalculatorService) {
        this.feeCalculatorService = feeCalculatorService;
    }
	  // STATIC field used by the static method
    private static DailyCoService dailyCoService;

    // Let Spring inject the bean into the static field
    @Autowired
    public void setDailyCoService(DailyCoService svc) {
        AppointmentDaoImpl.dailyCoService = svc;
    }
	//To add a new Appointment
	public static HashMap<String, String> setAppointment(HashMap<String, Object> appointmentMap) {
    Session session = null;
    Transaction tx = null;
    HashMap<String, String> res = new HashMap<>();
	LocalTime startTime = null;
	Long appointmentCount = 0L;
	String currencyName="";
    try {
        session = HibernateUtil.buildSessionFactory();
        tx = session.beginTransaction();

   //      Retrieve appointment count
			 // Retrieve appointment count
        Object[] row = (Object[]) session.createNativeQuery(
        	    "SELECT " +
        	    "   (SELECT COUNT(*) FROM Appointment a WHERE a.UserID = :userId and a.Status=4) AS appointment_count, " +
        	    "   cc.currency_name " +
        	    "FROM registration r " +
        	    "LEFT JOIN allcures_schema.countries_currencies cc " +
        	    "       ON UPPER(cc.country_code) = UPPER(r.country_code) " +
        	    "WHERE r.registration_id = :userId")
        	    .setParameter("userId", (Integer) appointmentMap.get("userID"))
        	    .uniqueResult();
			if (row != null) {
        		Number apptNum  = (Number) row[0];
                long apptCountLong = (apptNum != null) ? apptNum.longValue() : 0L;
                appointmentCount = apptCountLong;
        	    currencyName   = (String) row[1];

        	    appointmentMap.put("currency", currencyName != null ? currencyName : "INR");
        	}

        // Query<Long> query = session.createQuery(
        //     "SELECT COUNT(a) FROM Appointment a WHERE a.userID = :userID", Long.class);
        // query.setParameter("userID", (Integer) appointmentMap.get("userID"));
        // Long appointmentCount = query.uniqueResult();
		
        // Create new appointment
		 Integer userId = (Integer)(appointmentMap.get("userID"));
      	Integer docId = (Integer)(appointmentMap.get("docID"));
       
        Appointment appointment = new Appointment();
        appointment.setDocID(docId);
        appointment.setUserID(userId);

        // Parse and set appointment date
        String dateString = (String) appointmentMap.get("appointmentDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = dateFormat.parse(dateString);
        appointment.setAppointmentDate(new java.sql.Date(parsedDate.getTime()));

        // Retrieve doctor's availability
        AvailabilitySchedule doctorAvailability = session.get(
            AvailabilitySchedule.class, (Integer) appointmentMap.get("docID"));

        if (doctorAvailability != null) {
            int slotDuration = doctorAvailability.getSlotDuration();
            startTime = LocalTime.parse((String) appointmentMap.get("startTime"));
            LocalTime endTime = startTime.plusMinutes(slotDuration);

            appointment.setStartTime(startTime.toString());
            appointment.setEndTime(endTime.toString());
        } else {
            throw new Exception("Doctor availability not found for docID: " + appointmentMap.get("docID"));
        }

        // Set other appointment details
        appointment.setPaymentStatus((Integer) appointmentMap.get("paymentStatus"));
        appointment.setStatus(0);

        // Save the appointment
        session.save(appointment);
        tx.commit();

         // ==== Free vs Paid logic ====
               if (appointmentCount < 2 
        	    || (currencyName != null && !currencyName.trim().isEmpty() 
        	        && !"INR".equalsIgnoreCase(currencyName))) {
            // FREE: create meeting & send email; DO NOT take payment
            String meeting = null;
            try {
                DailyCoService svc = new DailyCoService(new org.springframework.web.client.RestTemplate());
                meeting = svc.createMeeting(null, appointment);
            } catch (Exception ex) {
                ex.printStackTrace();
                res.put("MeetingError", "Failed to create meeting link: " + ex.getMessage());
            }

            if (meeting != null && !meeting.isEmpty()) {
                // Format time to 12-hour with AM/PM
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                java.util.Date time = inputFormat.parse(startTime.toString());
                String formattedTime = outputFormat.format(time).toUpperCase(Locale.ENGLISH);

                try {
                    VideoDaoImpl.sendEmail(docId, userId, meeting, dateString, formattedTime);
                } catch (Exception mailEx) {
                    mailEx.printStackTrace();
                    res.put("EmailError", "Failed to send email: " + mailEx.getMessage());
                }
            }

            // Indicate free path
            res.put("Count", "0"); // 0 => free (under 2)
        } else {
            // PAID: initiate payment
            HashMap<String, String> payRes = PaymentGatewayDaoImpl.setPayment(appointmentMap, appointment.getAppointmentID());
            if (payRes != null) res.putAll(payRes);
            res.put("Count", "1"); // 1 => paid (2 or more)
        }
        return res;
    } catch (Exception e) {
       if (tx != null && tx.getStatus().canRollback()) {
        tx.rollback();
    }
        e.printStackTrace(); // Replace with proper logging
        res.put("Error", "Failed to set appointment: " + e.getMessage());
        return res;
    } 
}



	
	//To get all the Appointments
	public static List<Appointment> getAppointments() {
		Session session = HibernateUtil.buildSessionFactory();
		Query query1 = session.createNativeQuery(
				"SELECT AppointmentID, DocID, UserID, AppointmentDate, StartTime, EndTime, RequestStatus, PaymentStatus, FailureID, CreatedDate, LastUpdatedDate, Status" 
 		  + " FROM Appointment;");
		List<Appointment> AppointmentList = new ArrayList<>();

		List<Object[]> resultList = query1.getResultList();
		Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

		for (Object[] obj : resultList) {
			Appointment appointment = new Appointment();
			appointment.setAppointmentID(obj[0] != null ? (Integer) obj[0] : 0);
			appointment.setDocID(obj[1] != null ? (Integer) obj[1] : 0);
			appointment.setUserID(obj[2] != null ? (Integer) obj[2] : 0);
			 if (obj[3] != null) {
	            Timestamp ts = (Timestamp) obj[3];
	            appointment.setAppointmentDate(new Date(ts.getTime()));
	        } else {
	            appointment.setAppointmentDate(null);
	        }
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
	    StringBuilder sql = new StringBuilder(
	        "SELECT " +
	        "a.AppointmentID, " +           // 0
	        "a.DocID, " +                   // 1
	        "a.UserID, " +                  // 2
	        "a.AppointmentDate, " +         // 3
	        "a.StartTime, " +               // 4
	        "a.EndTime, " +                 // 5
	        "a.RequestStatus, " +           // 6
	        "a.PaymentStatus, " +           // 7
	        "a.FailureID, " +               // 8
	        "a.CreatedDate, " +             // 9
	        "a.LastUpdatedDate, " +         // 10
	        "a.Status, " +                  // 11
	        "a.MeetingLink, " +             // 12
	        "CONCAT(d.docname_first, ' ', d.docname_middle, ' ', d.docname_last) AS DoctorName, " + // 13
	        "da.SlotDuration, " +           // 14
	        "d.img_Loc, " +                 // 15
	        "m.name, " +                    // 16
	        "a.IsPaid, " +                  // 17 (paid)
	        // Prescription fields
	        "p.file_path " +                 // 18
	        "FROM Appointment a " +
	        "LEFT JOIN Doctors_New d ON a.DocID = d.docid " +
	        "LEFT JOIN DoctorAvailability da ON a.DocID = da.DocID " +
	        "LEFT JOIN medicinetype m ON m.id = d.MedicineTypeID " +
	        // Robust prescription join: join to a derived table for latest ACTIVE prescription per appointment
	        "LEFT JOIN ( " +
	        "    SELECT p1.pres_id, p1.appointment_id, p1.file_path FROM prescription p1 " +
	        "    INNER JOIN ( " +
	        "        SELECT appointment_id, MAX(uploaded_at) AS max_uploaded_at " +
	        "        FROM prescription " +
	       
	        "        GROUP BY appointment_id " +
	        "    ) p2 ON p1.appointment_id = p2.appointment_id AND p1.uploaded_at = p2.max_uploaded_at " +
	     
	        ") p ON p.appointment_id = a.AppointmentID " +
	        "WHERE a.DocID = :docID"
	    );
	    Query query = session.createNativeQuery(sql.toString());
	    query.setParameter("docID", docID);
	    List<Appointment> AppointmentList = new ArrayList<>();
	    List<Object[]> resultList = query.getResultList();
	    Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);
	    for (Object[] obj : resultList) {
	        Appointment appointment = new Appointment();
	        appointment.setAppointmentID(obj[0] != null ? ((Number) obj[0]).intValue() : 0);
	        appointment.setDocID(obj[1] != null ? ((Number) obj[1]).intValue() : 0);
	        appointment.setUserID(obj[2] != null ? ((Number) obj[2]).intValue() : 0);
	         if (obj[3] != null) {
	            Timestamp ts = (Timestamp) obj[3];
	            appointment.setAppointmentDate(new Date(ts.getTime()));
	        } else {
	            appointment.setAppointmentDate(null);
	        }
	        appointment.setStartTime(obj[4] != null ? (String) obj[4] : "");
	        appointment.setEndTime(obj[5] != null ? (String) obj[5] : "");
	        appointment.setRequestStatus(obj[6] != null ? ((Number) obj[6]).intValue() : 0);
	        appointment.setPaymentStatus(obj[7] != null ? ((Number) obj[7]).intValue() : 0);
	        appointment.setFailureID(obj[8] != null ? ((Number) obj[8]).intValue() : 0);
	        appointment.setCreatedDate(obj[9] != null ? (Timestamp) obj[9] : null);
	        appointment.setLastUpdatedDate(obj[10] != null ? (Timestamp) obj[10] : null);
	        appointment.setStatus(obj[11] != null ? ((Number) obj[11]).intValue() : 0);
	        appointment.setMeetingLink(obj[12] != null ? (String) obj[12] : null);
	        appointment.setDoctorName(obj[13] != null ? (String) obj[13] : null);
	        appointment.setSlotDuration(obj[14] != null ? ((Number) obj[14]).intValue() : null);
	        appointment.setImgLoc(obj[15] != null ? (String) obj[15] : null);
	        appointment.setMedicineType(obj[16] != null ? (String) obj[16] : null);
	        // paid (IsPaid)
	        if (obj[17] != null) {
	            if (appointment.getClass().getDeclaredFields() != null) {
	                try { appointment.getClass().getMethod("setIsPaid", Boolean.class).invoke(appointment, (Boolean) obj[17]); } catch (Exception e) { /* ignore if not present */ }
	            }
	        }
	        // Prescription file path
	        if (obj[18] != null) {
	            Prescription pres = new Prescription();
	            String filePath = (String) obj[18];
	            if (filePath != null && !filePath.isEmpty()) {
	                // Extract date and filename from the file path
	                // Example: /home/uat/Production/installers/tomcat/webapps/cures_articleimages/prescription/2025-11-04/1762236304679_3cae6cf4-23f0-4553-a967-baea5ad46c50_eg.webp
	                String[] parts = filePath.replace("\\", "/").split("/prescription/");
	                if (parts.length == 2) {
	                    String[] dateAndFile = parts[1].split("/", 2);
	                    if (dateAndFile.length == 2) {
	                        String date = dateAndFile[0];
	                        String filename = dateAndFile[1];
	                        filePath = "https://uat.all-cures.com/cures_articleimages/prescription/" + date + "/" + filename;
	                    } else {
	                        // fallback: just use filename
	                        filePath = "https://uat.all-cures.com/cures_articleimages/prescription/" + dateAndFile[0];
	                    }
	                } else {
	                    // fallback: just use the filename
	                    int lastSlash = filePath.lastIndexOf("/");
	                    String filename = lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
	                    filePath = "https://uat.all-cures.com/cures_articleimages/prescription/" + filename;
	                }
	            } else {
	                filePath = null;
	            }
	            pres.setFilePath(filePath);
	            appointment.setPrescription(pres);
	        } else {
	            appointment.setPrescription(null);
	        }
	        AppointmentList.add(appointment);
	    }
	    return AppointmentList;
	}
	

	//To get Appointments of a particular User
		public static List<Appointment> getAppointmentsOfUser(Integer docID,Integer userID) {
			Session session = HibernateUtil.buildSessionFactory();
			Query query1 = session.createNativeQuery(
					"SELECT AppointmentID, DocID, UserID, AppointmentDate, StartTime, EndTime, "
 +"RequestStatus, PaymentStatus, FailureID, CreatedDate, LastUpdatedDate, Status "
   +" FROM Appointment where DocID=" +docID + " AND UserID= " + userID + ";");
			List<Appointment> AppointmentList = new ArrayList<>();

			List<Object[]> resultList = query1.getResultList();
			Constant.log("Executed Query and Got: " + resultList.size() + " Appointment Lists back", 1);

			for (Object[] obj : resultList) {
				Appointment appointment = new Appointment();
				appointment.setAppointmentID(obj[0] != null ? (Integer) obj[0] : 0);
				appointment.setDocID(obj[1] != null ? (Integer) obj[1] : 0);
				appointment.setUserID(obj[2] != null ? (Integer) obj[2] : 0);
				 if (obj[3] != null) {
	            Timestamp ts = (Timestamp) obj[3];
	            appointment.setAppointmentDate(new Date(ts.getTime()));
	        } else {
	            appointment.setAppointmentDate(null);
	        }
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
            "a.AppointmentID, " +           // 0
            "a.DocID, " +                   // 1
            "a.UserID, " +                  // 2
            "a.AppointmentDate, " +         // 3
            "a.StartTime, " +               // 4
            "a.EndTime, " +                 // 5
            "a.RequestStatus, " +           // 6
            "a.MeetingLink, " +             // 7
            "a.IsPaid, " +                  // 8
            "a.PaymentStatus, " +           // 9
            "a.FailureID, " +               // 10
            "a.CreatedDate, " +             // 11
            "a.LastUpdatedDate, " +         // 12
            "a.Status, " +                  // 13
            "CONCAT(d.docname_first, ' ', d.docname_middle, ' ', d.docname_last) AS DoctorName, " + // 14
            "da.SlotDuration, " +           // 15
            "d.img_Loc, " +                 // 16
            "m.name, " +                    // 17
            // Prescription fields
            "p.pres_id, " +                 // 18
            "p.file_path, " +               // 19
            "p.original_name, " +           // 20
            "p.notes, " +                   // 21
            "p.issued_at, " +               // 22
            "p.uploaded_by, " +             // 23
            "p.uploaded_at, " +             // 24
            "p.status " +                   // 25
            "FROM Appointment a " +
            "LEFT JOIN Doctors_New d ON a.DocID = d.docid " +
            "LEFT JOIN DoctorAvailability da ON a.DocID = da.DocID " +
            "LEFT JOIN medicinetype m ON m.id = d.MedicineTypeID " +
            // LEFT JOIN latest prescription per appointment (MySQL)
            "LEFT JOIN prescription p ON p.pres_id = ("
            + "    SELECT pres_id FROM prescription p2"
            + "    WHERE p2.appointment_id = a.AppointmentID"
            + "      AND p2.status = 'ACTIVE'"
            + "    ORDER BY p2.uploaded_at DESC"
            + "    LIMIT 1"
            + ") "
            + "WHERE (a.IsPaid = FALSE OR a.PaymentStatus = 1) AND a.UserID = :userID"
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
            appointment.setAppointmentID(obj[0] != null ? ((Number) obj[0]).intValue() : 0);
            appointment.setDocID(obj[1] != null ? ((Number) obj[1]).intValue() : 0);
            appointment.setUserID(obj[2] != null ? ((Number) obj[2]).intValue() : 0);
             if (obj[3] != null) {
	            Timestamp ts = (Timestamp) obj[3];
	            appointment.setAppointmentDate(new Date(ts.getTime()));
	        } else {
	            appointment.setAppointmentDate(null);
	        }
            appointment.setStartTime(obj[4] != null ? (String) obj[4] : "");
            appointment.setEndTime(obj[5] != null ? (String) obj[5] : "");
            appointment.setRequestStatus(obj[6] != null ? ((Number) obj[6]).intValue() : 0);
            appointment.setMeetingLink(obj[7] != null ? (String) obj[7] : "");
            appointment.setIsPaid(obj[8] != null ? ((Boolean) obj[8]) : false);
            appointment.setPaymentStatus(obj[9] != null ? ((Number) obj[9]).intValue() : 0);
            appointment.setFailureID(obj[10] != null ? ((Number) obj[10]).intValue() : 0);
            appointment.setCreatedDate(obj[11] != null ? (Timestamp) obj[11] : null);
            appointment.setLastUpdatedDate(obj[12] != null ? (Timestamp) obj[12] : null);
            appointment.setStatus(obj[13] != null ? ((Number) obj[13]).intValue() : 0);
            appointment.setDoctorName(obj[14] != null ? (String) obj[14] : "");
            appointment.setSlotDuration(obj[15] != null ? ((Number) obj[15]).intValue() : 0);
            appointment.setImgLoc(obj[16] != null ? (String) obj[16] : "");
            appointment.setMedicineType(obj[17] != null ? (String) obj[17] : "");
            // Prescription mapping (may be null if no prescription)
            if (obj[18] != null) {
                Prescription pres = new Prescription();
                pres.setPresId(obj[18] != null ? ((Number) obj[18]).intValue() : null);
                if (obj[19] != null) {
                    String filePath = (String) obj[19];
                    filePath = filePath.replace("/home/uat/Production/installers/tomcat/webapps/", "https://uat.all-cures.com/");
                    pres.setFilePath(filePath);
                } else {
                    pres.setFilePath(null);
                }
                pres.setOriginalName(obj[20] != null ? (String) obj[20] : null);
                pres.setNotes(obj[21] != null ? (String) obj[21] : null);
                if (obj[22] != null) {
                    Object issuedObj = obj[22];
                    if (issuedObj instanceof java.sql.Timestamp) {
                        pres.setIssuedAt(((java.sql.Timestamp) issuedObj).toLocalDateTime());
                    } else if (issuedObj instanceof java.util.Date) {
                        pres.setIssuedAt(((java.util.Date) issuedObj).toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime());
                    }
                }
                pres.setUploadedBy(obj[23] != null ? ((Number) obj[23]).intValue() : null);
                if (obj[24] != null) {
                    Object upObj = obj[24];
                    if (upObj instanceof java.sql.Timestamp) {
                        pres.setUploadedAt(((java.sql.Timestamp) upObj).toLocalDateTime());
                    } else if (upObj instanceof java.util.Date) {
                        pres.setUploadedAt(((java.util.Date) upObj).toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime());
                    }
                }
                pres.setStatus(obj[25] != null ? (String) obj[25] : null);
                appointment.setPrescription(pres);
            } else {
                appointment.setPrescription(null);
            }
            AppointmentList.add(appointment);
        }
        return AppointmentList;
    }
	//To get Total , unbooked slots and Completely Booked Dates of a particular doctor
	public static Map<String, Object> findCompletelyBookedAndAvailableDates(int doctorId, int userId) {
	    Map<String, Object> datesMap = new HashMap<>();
	    List<LocalDate> completelyBookedDates = new ArrayList<>();
	    Map<LocalDate, Set<LocalTime>> availableDates = new TreeMap<>();
	    Map<LocalDate, Set<LocalTime>> unbookedSlots = new TreeMap<>();
		  BigDecimal amount = null;
		 String country_code=null;
		String CurrencySymbol=null;
		 Long appointmentCount = 0L;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();

	        // Check if the doctor exists
	    Query query = session.createNativeQuery(
	                "SELECT sc.Fee,d.docname_first,  (\n"
	                + "    SELECT COUNT(*) "
	                + "    FROM Appointment a "
	                + "    WHERE a.UserID  = :userId and a.Status=4 "
	                + "  ) AS appointment_count, reg.country_code,cc.currency_symbol "
	                +
	                "FROM allcures_schema.ServiceContractDetails sc " +
	                "JOIN registration r ON r.registration_id = sc.UserID " +
					 "JOIN registration reg ON reg.registration_id = :userId " +
					"LEFT JOIN countries_currencies cc ON cc.country_code = reg.country_code " +
	                "JOIN Doctors_New d ON d.docid = r.DocID " +
	                "WHERE sc.ServiceID=2 AND d.DocID = :doctorId"
	            );
	        query.setParameter("doctorId", doctorId);
	        query.setParameter("userId", userId);
	        List<Object[]> resultList = query.getResultList();

	        if (!resultList.isEmpty()) {
	            // Doctor found
				
			for (Object[] row : resultList) {
	    	                // Assuming the fee is the first column and doctor's name is the second column in the result set
	    	                amount = row[0] != null ? (BigDecimal) row[0] : BigDecimal.ZERO;
							 country_code = row[3] != null ? (String) row[3] : "";
							CurrencySymbol = row[4] != null ? (String) row[4] : "";
							 Number apptNum  = (Number) row[2];
	    	                 long apptCountLong = (apptNum != null) ? apptNum.longValue() : 0L;
	    	                 appointmentCount = apptCountLong;
	    	                 // Your isPaid logic (avoid NPEs)
	    	                 boolean isPaid = apptCountLong >= 2;
	    	               

	    	                 // If you also need amount depending on countryCode
	    	                if (country_code == null || country_code.trim().isEmpty()
	    	                         || "IN".equalsIgnoreCase(country_code)) {
	    	                     BigDecimal baseFee = feeCalculatorService.toBigDecimal(row[0]);

	    	     				BigDecimal totalFee = feeCalculatorService.calculateTotalFee(baseFee);

	    	     				// build breakdown map
	    	     				Map<String, BigDecimal> breakdown = feeCalculatorService.buildBreakdown(totalFee);

	    	     				// create nested object for "fee"
	    	     				Map<String, Object> feeObject = new HashMap<>();
	    	     				feeObject.putAll(breakdown);    // gst, baseFee, etc.

	    	     				// put into doctor
	    	     				datesMap.put("amount", feeObject);
	    	                     datesMap.put("currency_symbol", "₹ ");
								  datesMap.put("isPaid", isPaid);              // or Boolean.toString(isPaid)
	    	                 } else {
	    	                     datesMap.put("amount", "0");
	    	                     datesMap.put("currency_symbol", CurrencySymbol + " ");
								  datesMap.put("isPaid", false);              
	    	                 }
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

	private static TreeSet<LocalTime> calculateTotalSlots(int doctorId) {
		TreeSet<LocalTime> slotStartTimes = new TreeSet<>();
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
//	            System.out.println(slotStartTimes);
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


	private static TreeSet<LocalTime> getAppointmentsStartTimesForDate(int docID, LocalDate date) throws SQLException {
	    TreeSet<LocalTime> appointmentStartTimes = new TreeSet<>();
	    Session session = HibernateUtil.buildSessionFactory();
	    try {
	        Query query = session.createNativeQuery(
	            "SELECT StartTime FROM Appointment WHERE docid = " + docID + "   AND AppointmentDate = '"  + date +  "' AND status IN (1, 2)");
	        
	        List<Object> resultList = query.getResultList();
	        for (Object startTimeObj : resultList) {
//	            System.out.println("Type of startTimeObj: " + startTimeObj.getClass().getName());
	        }
//	        System.out.println(resultList.size());
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
//	    System.out.println("appointmentStartTimes"+appointmentStartTimes);
	    return appointmentStartTimes;
	}

	
}
