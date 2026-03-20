package service;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class AuditService {

    public void log(Session session,
                    Integer userId,
                    Integer doctorId,
                    Long slotId,
                    String action,
                    String status,
                    String message) {

        session.createNativeQuery(
                "INSERT INTO booking_audit_log " +
                "(user_id, doctor_id, slot_id, action, status, message) " +
                "VALUES (:userId, :doctorId, :slotId, :action, :status, :message)")
                .setParameter("userId", userId)
                .setParameter("doctorId", doctorId)
                .setParameter("slotId", slotId)
                .setParameter("action", action)
                .setParameter("status", status)
                .setParameter("message", message)
                .executeUpdate();
    }
}