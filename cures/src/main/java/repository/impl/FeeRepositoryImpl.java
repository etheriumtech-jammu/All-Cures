package repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import repository.FeeRepository;
import util.HibernateUtil;

@Repository
public class FeeRepositoryImpl implements FeeRepository {

	 Session session = HibernateUtil.buildSessionFactory(); // returns Session

    @Override
    public Object[] getFeeDetails(Integer doctorId, Integer userId) {

        String sql =
            "SELECT sc.Fee, " +
             " reg.country_code, cc.currency_symbol " +
            "FROM ServiceContractDetails sc " +
            "JOIN registration r ON r.registration_id = sc.UserID " +
            "JOIN Doctors_New d ON d.docid = r.DocID " +
            "JOIN registration reg ON reg.registration_id = :userId " +
            "LEFT JOIN countries_currencies cc ON cc.country_code = reg.country_code " +
            "WHERE sc.ServiceID = 2 AND d.DocID = :doctorId";

        return (Object[]) session
                .createNativeQuery(sql)
                .setParameter("doctorId", doctorId)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
