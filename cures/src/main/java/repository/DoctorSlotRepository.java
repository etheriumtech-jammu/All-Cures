package repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import util.HibernateUtil;

@Repository
public class DoctorSlotRepository {

	  Session session = HibernateUtil.buildSessionFactory(); // returns Session

    // GET ALL SLOTS
    public List<Object[]> getAllSlots(Integer doctorId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT start_datetime, COUNT(*) " +
                     "FROM doctor_slots " +
                     "WHERE doctor_id = :doctorId " +
                     "AND DATE(start_datetime) BETWEEN :startDate AND :endDate " +
                     "GROUP BY start_datetime";

        return session.createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    // AVAILABLE SLOTS
    public List<String> getAvailableSlots(Integer doctorId, LocalDate date) {
        String sql = "SELECT start_datetime FROM doctor_slots " +
                     "WHERE doctor_id = :doctorId " +
                     "AND is_booked = false " +
                     "AND DATE(start_datetime) = :date";

        return session.createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getResultList();
    }

    // TOTAL SLOTS
    public int totalSlots(Integer doctorId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM doctor_slots " +
                     "WHERE doctor_id = :doctorId " +
                     "AND DATE(start_datetime) = :date";

        Number result = (Number) session
                .createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getSingleResult();

        return result.intValue();
    }

    // BOOKED SLOTS
    public int bookedSlots(Integer doctorId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM doctor_slots " +
                     "WHERE doctor_id = :doctorId " +
                     "AND DATE(start_datetime) = :date " +
                     "AND is_booked = true";

        Number result = (Number) session
                .createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getSingleResult();

        return result.intValue();
    }

    public void insertIgnore(Integer doctorId, LocalDateTime start, LocalDateTime end) {

        String sql = "INSERT INTO doctor_slots (doctor_id, start_datetime, end_datetime) " +
                     "VALUES (:doctorId, :start, :end) " +
                     "ON DUPLICATE KEY UPDATE doctor_id = doctor_id";

        System.out.println("Inserting slot: " + doctorId + " | " + start + " - " + end);

        Session session = HibernateUtil.buildSessionFactory();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.createNativeQuery(sql)
                    .setParameter("doctorId", doctorId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .executeUpdate();

            tx.commit();   // ✅ VERY IMPORTANT

            System.out.println("Slot inserted/ignored successfully.");

        } catch (Exception e) {

            if (tx != null) tx.rollback();  // ✅ rollback on failure

            System.out.println("Error inserting slot: " + e.getMessage());

        } 
    }

    // DELETE OLD
    public void deleteOldSlots() {
        String sql = "DELETE FROM doctor_slots WHERE start_datetime < NOW()";
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

         
        session
                .createNativeQuery(sql)
                .executeUpdate();
        tx.commit();   // ✅ VERY IMPORTANT

        System.out.println("Slots deleted successfully.");
        } catch (Exception e) {

			if (tx != null) tx.rollback();  // ✅ rollback on failure

			System.out.println("Error deleting old slots: " + e.getMessage());

		}
    }

    // RANGE
    public List<Object[]> getSlotsInRange(Integer doctorId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT start_datetime, is_booked FROM doctor_slots " +
                     "WHERE doctor_id = :doctorId " +
                     "AND DATE(start_datetime) BETWEEN :startDate AND :endDate";

        return session
                .createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

}
