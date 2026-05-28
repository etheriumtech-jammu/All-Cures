package dao;

import org.hibernate.Query;
import org.hibernate.Session;

import model.Registration;
import util.HibernateUtil;

public class LoginDao {

   
    public Registration loginWithEmail(
            String email,
            String password) {

        Session session =
                HibernateUtil
                        .getSessionFactory()
                        .openSession();

        try {

            Query query = session.createQuery(
                    "FROM Registration " +
                    "WHERE email_address = :email " +
                    "AND pass_word = :password"
            );

            query.setParameter("email", email);

            query.setParameter("password", password);

            return (Registration)
                    query.uniqueResult();
        }
         finally {

		//	session.close();
		}
        
    }


    public Registration loginWithMobile(
            Long mobile,
            String password) {

        Session session =
                HibernateUtil
                        .getSessionFactory()
                        .openSession();

        
        try {

            Query query = session.createQuery(
                    "FROM Registration " +
                    "WHERE mobile_number = :mobile " +
                    "AND pass_word = :password"
            );

            query.setParameter("mobile", mobile);

            query.setParameter("password", password);

            return (Registration)
                    query.uniqueResult();

        } finally {

            session.close();
        }
    }

 
    public Registration loginWithOtp(
            Long mobile,
            String otp) {

        Session session =
                HibernateUtil
                        .getSessionFactory()
                        .openSession();

        try {

            Query query = session.createQuery(
                    "FROM Registration " +
                    "WHERE mobile_number = :mobile " +
                    "AND otp = :otp"
            );

            query.setParameter("mobile", mobile);

            query.setParameter("otp", otp);

            return (Registration)
                    query.uniqueResult();

        } finally {

            session.close();
        }
    }
}