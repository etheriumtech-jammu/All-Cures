package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Registration;
import util.HibernateUtil;

public class AuthDao {

    // ===========================
    // SAVE REGISTRATION
    // ===========================

    public static Registration saveRegistration(
            String firstName,
            String lastName,
            String password,
            String email,
            Boolean acceptTerms,
            Integer registrationType,
            Boolean privacyPolicy,
            Integer accountState,
            Integer rememberMe,
            Long mobileNumber,
            Integer age,
            String countryCode) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil
                    .buildSessionFactory();

            tx = session.beginTransaction();

            Registration user = new Registration();

            // ===========================
            // SET MODEL VALUES
            // ===========================

            user.setFirst_name(firstName);

            user.setLast_name(lastName);

            // EMAIL OPTIONAL
            user.setEmail_address(email);

            user.setPass_word(password);

            System.out.println("Accept Terms: " + acceptTerms);
            user.setAcceptance_condition(acceptTerms);

            user.setRegistration_type(registrationType);

            user.setprivacy_policy(privacyPolicy);

            user.setAccount_state(accountState);

            user.setRemember_me(rememberMe);

            user.setMobile_number(mobileNumber);

            user.setAge(age);

            user.setCountry_code(countryCode);

            // DEFAULT VALUES

            user.setLogin_attempt(0);

            // SAVE ENTITY

            session.save(user);

            tx.commit();

            return user;

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();

        } finally {


        }

        return null;
    }

    // ===========================
    // GET USER FROM MOBILE
    // ===========================

    public static Registration getUserFromMobile(
            Long mobile) {

        Session session = null;

        try {

            session = HibernateUtil.buildSessionFactory();

            String hql =
                    "FROM Registration " +
                            "WHERE mobile_number = :mobile";

            Query<Registration> query =
                    session.createQuery(hql, Registration.class);

            query.setParameter("mobile", mobile);

            return query.uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

        }

        return null;
    }

    // ===========================
    // GET USER FROM EMAIL
    // ===========================

    public static Registration getUserFromEmail(
            String email) {

        Session session = null;

        try {

            session = HibernateUtil.buildSessionFactory();

            String hql =
                    "FROM Registration " +
                            "WHERE email_address = :email";

            Query<Registration> query =
                    session.createQuery(hql, Registration.class);

            query.setParameter("email", email);

            return query.uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {


        }

        return null;
    }

    // ===========================
    // LOGIN USER
    // ===========================

    public static Registration loginUser(
            String username) {

        Session session = null;

        try {

            session = HibernateUtil.buildSessionFactory();

            Registration user = null;

            // MOBILE LOGIN

            if (username.matches("\\d+")) {

                String hql =
                        "FROM Registration " +
                                "WHERE mobile_number = :mobile";

                Query<Registration> query =
                        session.createQuery(hql, Registration.class);

                query.setParameter(
                        "mobile",
                        Long.parseLong(username)
                );

                user = query.uniqueResult();

            } else {

                // EMAIL LOGIN

                String hql =
                        "FROM Registration " +
                                "WHERE email_address = :email";

                Query<Registration> query =
                        session.createQuery(hql, Registration.class);

                query.setParameter("email", username);

                user = query.uniqueResult();
            }

            return user;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }
}