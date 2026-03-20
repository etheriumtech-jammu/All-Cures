package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.AvailabilitySchedule;
import util.HibernateUtil;

@Repository
public class AvailabilityRepository {

	 Session session = HibernateUtil.buildSessionFactory(); // returns Session


    public List<AvailabilitySchedule> findAllActiveDoctors() {

        String sql = "SELECT * FROM doctoravailability WHERE Status = 1";

        return session
                .createNativeQuery(sql, AvailabilitySchedule.class)
                .getResultList();
    }
}