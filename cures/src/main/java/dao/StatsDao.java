package dao;


import org.hibernate.Session;
import java.util.stream.Collectors;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
public class StatsDao {
		public static final int STATUS_PENDING = 1; // upcoming
	public static final int STATUS_SUCCESS = 4;
	public static final int STATUS_FAILED = 5;

	 public static BigInteger getAppointmentsSummaryByStatus(int status) {
	        Session session = HibernateUtil.buildSessionFactory();
	        Query query = session.createNativeQuery(
	            "SELECT COUNT(AppointmentID) FROM Appointment a WHERE a.Status = :status"
	        );
	        query.setParameter("status", status);

	        return (BigInteger) query.getSingleResult();
	    }

    public static List<Object[]> getMonthlyAppointments(int year) {
        Session session = HibernateUtil.buildSessionFactory();
        String sql = "SELECT MONTH(date_time), COUNT(*) " +
                     "FROM Appointment " +
                     "WHERE YEAR(date_time) = :year " +
                     "GROUP BY MONTH(date_time)";

        Query query = session.createNativeQuery(sql);
        query.setParameter("year", year);

        return query.getResultList();
    }
    
    public static BigInteger getAppointmentsCount(String startDate, String endDate) {
        Session session = HibernateUtil.buildSessionFactory();

        String sql = "SELECT COUNT(*) FROM Appointment " +
                     "WHERE DATE(AppointmentDate) BETWEEN :startDate AND :endDate AND Status=4";

        Query query = session.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return (BigInteger) query.getSingleResult();
    }
    
    
    // Returns a list of maps, one map per row
    public static Map<String, Object> listSucceededAppointments(String startDate, String endDate, Integer docId, Integer limit, Integer offset, Integer status) {
        Session session = HibernateUtil.buildSessionFactory(); // keeping your pattern
        StringBuilder sql = new StringBuilder ("SELECT a.AppointmentID, " + 
                "a.DocID, " +
                "a.UserID, " +
                "a.AppointmentDate, " +
                "a.StartTime, " +
                "a.EndTime, " +
                "a.meetingLink, " +
                "a.IsPaid, " +
                "d.docname_first, " +
                "d.docname_middle, " +
                "d.docname_last, " +
                "r.first_name AS user_first_name," +
                "r.last_name  AS user_last_name, " +
                "sc.Fee,asm.StatusType, COUNT(*) OVER() AS total_count " +
            "FROM allcures_schema.Appointment a "+
            "JOIN Doctors_New d ON d.docid = a.DocID " +
            "JOIN AppointmentStatusMaster asm ON asm.AppointmentStatusID=a.Status " +
            "JOIN registration r ON r.registration_id = a.UserID " +
            "JOIN registration reg ON reg.DocID = a.DocID " +
            "JOIN ServiceContractDetails sc ON sc.UserID = reg.registration_id " +
   //         " WHERE a.Status = 4 AND sc.ServiceID = 2 AND DATE(AppointmentDate) BETWEEN :startDate AND :endDate");
        		 " WHERE sc.ServiceID = 2 AND DATE(AppointmentDate) BETWEEN :startDate AND :endDate");

        if (docId != null) {
            sql.append(" AND a.DocID = :docId ");
        }
        if (status != null) {
            sql.append(" AND a.Status = :status ");
        }
        sql.append(" ORDER BY a.AppointmentDate DESC, a.StartTime DESC ");
        sql.append(" LIMIT :limit OFFSET :offset ");
        Query query = session.createNativeQuery(sql.toString());
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);
        if (docId != null)     query.setParameter("docId", docId);
        if (status != null)     query.setParameter("status", status);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        long totalRecords = 0;
        if (!rows.isEmpty()) {
            Object[] first = rows.get(0);
            // total_count is the last selected column
            Number n = (Number) first[first.length - 1];
            totalRecords = (n == null) ? 0L : n.longValue();
        }
        long totalPages = (totalRecords + limit - 1) / limit;  // ceil(total/size)

        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : rows) {
            int i = 0;
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("appointmentId",   row[i++]);                // a.AppointmentID (Integer/Long)
            m.put("docId",           row[i++]);                // a.DocID
            m.put("userId",          row[i++]);                // a.UserID
            m.put("appointmentDate", (Date) row[i++]);         // a.AppointmentDate
            m.put("startTime",       (String) row[i++]);       // a.StartTime
            m.put("endTime",         (String) row[i++]);       // a.EndTime
            m.put("meetingLink",     (String) row[i++]);       // a.meetingLink
            m.put("isPaid",          row[i++]);                // a.IsPaid (TinyInt -> Integer/Boolean)
            String docFirst  = (String) row[i++];
            String docMiddle = (String) row[i++];
            String docLast   = (String) row[i++];
            String doctorFullName = String.join(" ",
                    Arrays.asList(docFirst, docMiddle, docLast)
                          .stream()
                          .filter(Objects::nonNull)
                          .filter(s -> !s.isBlank())
                          .collect(Collectors.toList()));
            m.put("doctorName", doctorFullName.isBlank() ? null : doctorFullName);

            String userFirst = (String) row[i++];
            String userLast  = (String) row[i++];
            String userFullName = String.join(" ",
                    Arrays.asList(userFirst, userLast)
                          .stream()
                          .filter(Objects::nonNull)
                          .filter(s -> !s.isBlank())
                          .collect(Collectors.toList()));
            m.put("userName", userFullName.isBlank() ? null : userFullName);
            m.put("fee",             (BigDecimal) row[i++]);   // sc.Fee
            m.put("Status",     (String)     row[i++]);  
         // skip the window column in items (it’s at row[i] now)
            i++; // total_count
            out.add(m);
           
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("result", out);
        result.put("totalPages", totalPages);
        return result;
    }

	 // 1) Active doctors
    // Helper: apply CreatedDate filter only when startDate != null or endDate != null.
    private static void appendCreatedDateFilter(StringBuilder sql, String alias, LocalDate startDate, LocalDate endDate) {
        if (startDate != null || endDate != null) {
            sql.append(" AND ").append(alias).append(".CreatedDate ");
            if (startDate != null) {
                sql.append(">= :startDate ");
                if (endDate != null) {
                    sql.append("AND ").append(alias).append(".CreatedDate <= :endDate ");
                } else {
                    sql.append("AND ").append(alias).append(".CreatedDate <= CURRENT_DATE ");
                }
            } else {
                // only end provided
                sql.append("<= :endDate ");
            }
        }
    }

    private static void bindCreatedDateParams(Query query, LocalDate startDate, LocalDate endDate) {
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
    }

    // 1) Active doctors with optional CreatedDate filter on Doctors_New
    public static BigInteger countActiveDoctors(LocalDate startDate, LocalDate endDate) {
        Session session = HibernateUtil.buildSessionFactory();
        Object result = null;
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                "FROM Doctors_New d " +
                "WHERE d.MedicineTypeID IS NOT NULL " +
                "  AND (d.docid <= 63 OR d.docid >= 14487)"
            );
            appendCreatedDateFilter(sql, "d", startDate, endDate);

            Query query = session.createNativeQuery(sql.toString());
            bindCreatedDateParams(query, startDate, endDate);

            result = query.getSingleResult();
            return toBigInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        } 
    }

    // 2) Signed doctors with optional CreatedDate filter on ServiceContractDetails
    public static BigInteger countSignedDoctors(LocalDate startDate, LocalDate endDate) {
        Session session = HibernateUtil.buildSessionFactory();
        Object result = null;
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT r.DocID) " +
                "FROM registration r " +
                "JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "WHERE sr.ServiceID = 2 " +
                "  AND sr.EndDate >= CURRENT_DATE"
            );
            appendCreatedDateFilter(sql, "sr", startDate, endDate);

            Query query = session.createNativeQuery(sql.toString());
            bindCreatedDateParams(query, startDate, endDate);

            result = query.getSingleResult();
            return toBigInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        } 
    }

    // 3) Signed by MedicineType (CreatedDate filter on ServiceContractDetails or Doctors_New? Usually "newly signed" => sr.CreatedDate)
    public static List<Map<String, Object>> signedDoctorsByMedicineType(LocalDate startDate, LocalDate endDate) {
        Session session = HibernateUtil.buildSessionFactory();
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT m.name AS medicine_type, COUNT(DISTINCT r.DocID) AS total_signed " +
                "FROM Doctors_New d " +
                "JOIN registration r            ON r.DocID = d.docid " +
                "JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "JOIN medicinetype m            ON d.MedicineTypeID = m.id " +
                "WHERE sr.ServiceID = 2 " +
                "  AND sr.EndDate >= CURRENT_DATE " +
                "  AND d.MedicineTypeID IS NOT NULL"
            );
            // Filter by when the doctor was signed (sr.CreatedDate). If you instead need d.CreatedDate, switch alias to "d".
            appendCreatedDateFilter(sql, "sr", startDate, endDate);

            sql.append(" GROUP BY m.name ");
            sql.append(" ORDER BY m.name ");

            Query query = session.createNativeQuery(sql.toString());
            bindCreatedDateParams(query, startDate, endDate);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = (List<Object[]>) query.getResultList();

            ArrayList<Map<String, Object>> out = new ArrayList<>();
            Iterator<?> itr = rows.iterator();
            while (itr.hasNext()) {
                Object[] obj = (Object[]) itr.next();
                Map<String, Object> rec = new LinkedHashMap<>();
                rec.put("medicineTypeName", obj[0] == null ? null : String.valueOf(obj[0]));        // name
                rec.put("total",            obj[1] == null ? BigInteger.ZERO : toBigInt(obj[1]));   // count
                out.add(rec);
            }
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } 
    }

    private static BigInteger toBigInt(Object n) {
        if (n == null) return BigInteger.ZERO;
        if (n instanceof BigInteger) return (BigInteger) n;
        if (n instanceof Number) return BigInteger.valueOf(((Number) n).longValue());
        return new BigInteger(String.valueOf(n));
    }

	public static Map<String, Object> getAppointmentStats(LocalDate startDate, LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory();
		try {
			String dateCol = "a.AppointmentDate";

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ").append("  COUNT(*) AS total, ").append("  SUM(CASE WHEN a.Status = ")
					.append(STATUS_SUCCESS).append(" THEN 1 ELSE 0 END) AS success, ")
					.append("  SUM(CASE WHEN a.Status = ").append(STATUS_FAILED)
					.append(" THEN 1 ELSE 0 END) AS failed, ").append("  SUM(CASE WHEN a.Status = ")
					.append(STATUS_PENDING).append(" THEN 1 ELSE 0 END) AS upcoming, ")
					.append("  SUM(CASE WHEN (COALESCE(a.IsPaid,0)=1 OR COALESCE(a.PaymentStatus,0)=1) THEN 1 ELSE 0 END) AS paid, ")
					.append("  SUM(CASE WHEN (COALESCE(a.IsPaid,0)=1 OR COALESCE(a.PaymentStatus,0)=1) THEN 0 ELSE 1 END) AS free ")
					.append("FROM Appointment a ").append("WHERE 1=1 ");

			if (startDate != null && endDate != null) {
				sql.append(" AND ").append(dateCol).append(" >= :startDate ").append(" AND ").append(dateCol)
						.append(" <= :endDate ");
			} else if (startDate != null) {
				sql.append(" AND ").append(dateCol).append(" >= :startDate ").append(" AND ").append(dateCol)
						.append(" <= CURRENT_DATE ");
			} else if (endDate != null) {
				sql.append(" AND ").append(dateCol).append(" <= :endDate ");
			}

			Query query = session.createNativeQuery(sql.toString());
			if (startDate != null)
				query.setParameter("startDate", java.sql.Date.valueOf(startDate));
			if (endDate != null)
				query.setParameter("endDate", java.sql.Date.valueOf(endDate));

			Object[] row = (Object[]) query.getSingleResult();

			Map<String, Object> out = new LinkedHashMap<>();
			out.put("totalAppointments", toBigInt(row[0]));
			out.put("successAppointments", toBigInt(row[1]));
			out.put("failedAppointments", toBigInt(row[2]));
			out.put("upcomingAppointments", toBigInt(row[3]));
			out.put("paidAppointments", toBigInt(row[4]));
			out.put("freeAppointments", toBigInt(row[5]));
			return out;

		} catch (Exception e) {
// no return here
			throw (e instanceof RuntimeException) ? (RuntimeException) e
					: new RuntimeException("Failed to fetch appointment stats", e);
		}

	}
}
