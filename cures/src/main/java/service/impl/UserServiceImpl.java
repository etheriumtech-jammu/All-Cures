package service.impl;

import dto.OtpRequest;
import dto.RegisterRequest;
import model.Registration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.UserService;
import util.Constant;
import util.EnDeCryptor;
import util.HibernateUtil;

import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	  // 🔍 Find by mobile
    @Override
    public Registration findByMobile(Long mobile) {
		Session session = HibernateUtil.buildSessionFactory();
        String hql = "FROM Registration WHERE mobile_number = :mobile";
        try {
        	 Query<Registration> query = session.createQuery(hql, Registration.class);
             query.setParameter("mobile", mobile);
             return query.uniqueResult();

        }catch (Exception e) {
			Constant.log("Error finding user by mobile: " + e.getMessage(), 3);
			return null;
		}       
    }

    // ✅ Check if mobile exists
    @Override
    public boolean existsByMobile( Long mobile) {
        return findByMobile(mobile) != null;
    }

    @Override
    public Registration createOtpUser(OtpRequest request) {
    	Transaction tx = null;
    	try {
    		Session session = HibernateUtil.buildSessionFactory();
    	     tx = session.beginTransaction();
    	        Registration user = new Registration();

    	        // 📱 Basic Details
    	        user.setMobile_number(request.getMobile());
    	        user.setCountry_code(request.getCountryCode());

    	        user.setFirst_name(request.getFirstName());
    	        user.setLast_name(request.getLastName());

    	        // ✅ Accept Terms & Policy (String → Boolean)
    	        boolean accTerms = isAccepted(request.getAcceptTnC());
    	        boolean accPolicy = isAccepted(request.getAcceptPolicy());
    	        user.setAcceptance_condition(accTerms);
    	        user.setprivacy_policy(accPolicy);

    	        // 🧠 Account Setup
    	        user.setAccount_state(1); // ACTIVE
    	        user.setRegistration_method(2); // OTP
    	        user.setIs_mobile_verified(true);
    	        user.setAge(0);

    	        // 💾 Save using Hibernate session
    	        session.save(user);
    	        tx.commit();
    	        return user;
    	}
    	catch (Exception e) {
    		if(tx != null) tx.rollback(); // 🔥 ROLLBACK on error
    		Constant.log("Error creating OTP user: " + e.getMessage(), 3);
			return null;		
    	}
       
    }
    // 🔐 Password Registration
    @Override
    public Registration registerWithPassword(RegisterRequest request) {
    		  Transaction tx = null;
    	try {
    		 Session session = HibernateUtil.buildSessionFactory();
    		  	 tx = session.beginTransaction();
    		 if (existsByMobile(request.getMobile())) {
    	            throw new RuntimeException("User already exists");
    	        }

    	        Registration user = new Registration();

    	        // 📱 Basic Info
    	        user.setMobile_number(request.getMobile());
    	        user.setCountry_code(request.getCountryCode());

    	        user.setFirst_name(request.getFirstName());
    	        user.setLast_name(request.getLastName());

    	        // 🔐 Password Encryption
    	        final String secretKey = Constant.SECRETE;
    	        EnDeCryptor encryptor = new EnDeCryptor();

    	        if (request.getPassword() == null || request.getPassword().isEmpty()) {
    	            throw new RuntimeException("Password cannot be empty");
    	        }

    	        String hashedPass = encryptor.encrypt(request.getPassword(), secretKey);
    	        user.setPass_word(hashedPass);

    	        // ✅ Accept Terms & Policy
    	        boolean accTerms = isAccepted(request.getAcceptTnC());
    	        boolean accPolicy = isAccepted(request.getAcceptPolicy());
    	        user.setAcceptance_condition(accTerms);
    	        user.setprivacy_policy(accPolicy);

    	        // 🧠 Account Setup
    	        user.setAccount_state(1); // ACTIVE
    	        user.setRegistration_method(1); // PASSWORD
    	        user.setIs_mobile_verified(false); // until OTP verified (optional)

    	        // 💾 Save (IMPORTANT FIX)
    	        session.save(user);
    	        tx.commit();
    	        return user;
    	}catch (Exception e) {
    		if(tx != null) tx.rollback(); // 🔥 ROLLBACK on error
			Constant.log("Error in registerWithPassword: " + e.getMessage(), 3);
			throw new RuntimeException("Registration failed: " + e.getMessage());		
		}
       
    }

    // 🔐 Mobile + Password Login
    @Override
    public Registration validatePasswordLogin(Long mobile, String password) {

        Registration user = findByMobile(mobile);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getPass_word() == null) {
            throw new RuntimeException("Password not set. Use OTP login.");
        }

        return user;
    }

    	private boolean isAccepted(String value) {
        return value != null && (
            value.equalsIgnoreCase("ON") ||
            value.equalsIgnoreCase("YES") ||
            value.equalsIgnoreCase("TRUE") ||
            value.equals("1")
        );
    }
}