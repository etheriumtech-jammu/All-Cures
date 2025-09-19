package dao;

import org.hibernate.Session;
import java.util.stream.Collectors;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class StatsDao {

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
    public static BigInteger countActiveDoctors() {
        Session session = HibernateUtil.buildSessionFactory();
        Object result = null;
        try {
            Query query = session.createNativeQuery(
                "SELECT COUNT(*) " +
                "FROM Doctors_New d " +
                "WHERE d.MedicineTypeID IS NOT NULL " +
                "  AND (d.docid <= 63 OR d.docid >= 14487)"
            );
            result = query.getSingleResult();
            return toBigInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        } 
    }

    public static BigInteger countSignedDoctors() {
        Session session = HibernateUtil.buildSessionFactory();
        Object result = null;
        try {
            Query query = session.createNativeQuery(
                "SELECT COUNT(DISTINCT r.DocID) " +
                "FROM registration r " +
                "JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "WHERE sr.ServiceID = 2 " +
                "  AND sr.EndDate >= CURRENT_DATE"
            );
            result = query.getSingleResult();
            return toBigInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        }
    }

    // Signed by MedicineType (returns List<Map<String,Object>> with name + total)
    public static List<Map<String, Object>> signedDoctorsByMedicineType() {
        Session session = HibernateUtil.buildSessionFactory();
        try {
            Query query = session.createNativeQuery(
                "SELECT m.name AS medicine_type, COUNT(DISTINCT r.DocID) AS total_signed " +
                "FROM Doctors_New d " +
                "JOIN registration r            ON r.DocID = d.docid " +
                "JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "JOIN medicinetype m            ON d.MedicineTypeID = m.id " +
                "WHERE sr.ServiceID = 2 " +
                "  AND sr.EndDate >= CURRENT_DATE " +
                "  AND d.MedicineTypeID IS NOT NULL " +
                "GROUP BY m.name " +
                "ORDER BY m.name"
            );

            @SuppressWarnings("unchecked")
            List<Object[]> rows = (List<Object[]>) query.getResultList();

            ArrayList<Map<String, Object>> out = new ArrayList<>();
            Iterator<?> itr = rows.iterator();
            while (itr.hasNext()) {
                Object[] obj = (Object[]) itr.next();
                Map<String, Object> rec = new LinkedHashMap<>();
                rec.put("medicineTypeName", obj[0] == null ? null : String.valueOf(obj[0])); // name, not ID
                rec.put("total",            obj[1] == null ? BigInteger.ZERO : toBigInt(obj[1]));
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

}
