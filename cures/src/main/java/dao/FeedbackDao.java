package dao;

import model.Feedback;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dto.FeedbackDto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
public class FeedbackDao implements FeedbackDto {

	@Autowired
	private RegistrationDaoImpl_New registrationDaoImpl;
    @Override
    public Feedback create(Feedback feedback) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.buildSessionFactory();
            tx = session.beginTransaction();

            Serializable idObj = session.save(feedback); // same as Prescription snippet
            session.flush();

            tx.commit();

            Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : Long.valueOf(idObj.toString());
            Feedback created = session.get(Feedback.class, id);

            // ✅ if whatsapp alert selected and phone number is present
            if (Boolean.TRUE.equals(created.getWhatsappAlert())
                    && created.getPhonenumber() != null
                    && !created.getPhonenumber().trim().isEmpty()) {

                // prepare ns_map
                Map<String, Object> ns_map = new HashMap<>();
                ns_map.put("country_code",
                        created.getCountryCode() != null ? created.getCountryCode() : "+91"); // default India
                ns_map.put("nl_subscription_disease_id", "0");
                ns_map.put("nl_sub_type", 1);
                ns_map.put("nl_subscription_cures_id", "");
                long mobileNum;
                try {
                    mobileNum = Long.parseLong(created.getPhonenumber().replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid phone number: " + created.getPhonenumber());
                    mobileNum = 0L;
                }
                // ✅ call newsletter subscription logic
                try {
                    String result = registrationDaoImpl.subscribe(mobileNum, (HashMap<String, Object>) ns_map);
                    System.out.println("Newsletter subscribe result: " + result);
                } catch (Exception e) {
                    System.err.println("Failed to subscribe to newsletter: " + e.getMessage());
                }
            }

            return created;

        } catch (RuntimeException ex) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            throw ex;
        } 
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Feedback.class, id));
        }
    }

    @Override
    public Optional<Feedback> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Feedback> list = session.createQuery(
                    "FROM Feedback f WHERE f.email = :email ORDER BY f.createdAt DESC", Feedback.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .list();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        }
    }

    @Override
    public List<Feedback> findAll(int limit, int offset) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Feedback f ORDER BY f.createdAt DESC", Feedback.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        }
    }

    @Override
    public int update(Feedback feedback) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(feedback);
            session.flush();
            tx.commit();
            return 1;
        } catch (RuntimeException ex) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            throw ex;
        } 
    }

    @Override
    public int delete(Long id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            int deleted = session.createQuery("DELETE FROM Feedback f WHERE f.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
            return deleted;
        } catch (RuntimeException ex) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            throw ex;
        } 
    }
}
