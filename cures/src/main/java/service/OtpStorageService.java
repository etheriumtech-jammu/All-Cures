package service;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import exception.OtpException;
import model.OtpVerification;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.HibernateUtil;

@Service
@Transactional
public class OtpStorageService {

		 private static MemcachedClient mcc = null;

    private static final int OTP_EXPIRY = 5; // minutes
    // 🔑 Key generator
    private String getKey(String countryCode, Long mobile) {

        String cleanCountryCode = countryCode
                .replace("+", "")
                .replaceAll("[^0-9]", "");

        String cleanMobile = String.valueOf(mobile);

        return cleanCountryCode + ":" + cleanMobile;
    }

    // 🔌 Memcache init
    public static MemcachedClient initializeCacheClient() {
        try {
            Constant.log("Connecting to Memcache...", 0);

            mcc = new MemcachedClient(
                    new ConnectionFactoryBuilder()
                            .setDaemon(true)
                            .setFailureMode(FailureMode.Retry)
                            .build(),
                    AddrUtil.getAddresses(Constant.ADDRESS));

            Constant.log("Memcache connected", 0);

        } catch (IOException e) {
            Constant.log("Memcache connection failed", 3);
            e.printStackTrace();
        }
        return mcc;
    }

    // 📱 SAVE OTP
  
    public void saveOtp(String countryCode, Long mobile, String otp) {

        if (mcc == null) {
            initializeCacheClient();
        }
        Session session = null;
        Transaction tx = null;
        System.out.println(countryCode + mobile + otp + " saveOtp called");
        try {
        	session = HibernateUtil.buildSessionFactory();
      
        	tx=session.beginTransaction();
            OtpVerification entity = new OtpVerification();
            entity.setOtp(otp);
            entity.setCountryCode(countryCode.replace("+", "").trim());
            entity.setMobileNumber(mobile);
            entity.setCreatedTime(LocalDateTime.now());
            entity.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY));
            entity.setAttempts(0);
            entity.setIsUsed(0);

            session.save(entity);
            tx.commit();
            System.out.println("OTP saved to DB: " + entity.getCountryCode());
            String key = getKey(countryCode, mobile);
            mcc.set(key, OTP_EXPIRY * 60, otp);

        } catch (Exception e) {
            throw new OtpException("Failed to save OTP", e);
        }
    }

    // 🔍 GET OTP
    public String getOtp(String countryCode, Long mobile) {
        if (mcc == null) {
            initializeCacheClient();
        }
        String key = getKey(countryCode.replace("+", ""), mobile);
        Object cachedOtp = mcc.get(key);
        if (cachedOtp != null) {
        	System.out.println("OTP fetched from cache: " + cachedOtp.toString());
            return cachedOtp.toString();
        }
        Session session = null;
        Transaction tx = null;

        try {
        	session = HibernateUtil.buildSessionFactory();
            String hql = "FROM OtpVerification " +
                    "WHERE mobileNumber = :mobile " +
                    "AND countryCode = :countryCode " +
                    "AND expiryTime > :now " +
                    "AND isUsed = 0 " +
                    "ORDER BY createdTime DESC";

            Query<OtpVerification> query = session.createQuery(hql, OtpVerification.class);
            query.setParameter("mobile", mobile);
            query.setParameter("countryCode", countryCode.replace("+", ""));
            query.setParameter("now", LocalDateTime.now());
            query.setMaxResults(1);

            OtpVerification entity = query.uniqueResult();
            if (entity == null) return null;
            mcc.set(key, OTP_EXPIRY * 60, entity.getOtp());
            System.out.println("OTP fetched from DB and cached: " + entity.getOtp());
            System.out.println("OTP expiry time: " + entity.getId());
            return entity.getOtp();

        } catch (Exception e) {
            throw new OtpException("Failed to fetch OTP", e);
        }
    }

    // 🗑 DELETE OTP
    public void deleteOtp(String countryCode, Long mobile) {
    	  Session session = null;
          Transaction tx = null;

          try {
          	session = HibernateUtil.buildSessionFactory();
          	tx=session.beginTransaction();
            String hql = "DELETE FROM OtpVerification WHERE mobileNumber = :mobile AND countryCode = :countryCode";

            session
                    .createQuery(hql)
                    .setParameter("mobile", mobile)
                    .setParameter("countryCode", countryCode.replace("+", ""))
                    .executeUpdate();
            tx.commit();

        } catch (Exception e) {
            throw new OtpException("Failed to delete OTP", e);
        }
    }

    // ✅ MARK VERIFIED
    public void markVerified(String countryCode, Long mobile, String otp) {

        if (mcc == null) {
            initializeCacheClient();
        }
        Session session = null;
        Transaction tx = null;

        try {
        	session = HibernateUtil.buildSessionFactory();
            
            tx = session.beginTransaction();   // 🔥 REQUIRED

            String hql = "UPDATE OtpVerification SET isUsed = 1 " +
                    "WHERE mobileNumber = :mobile " +
                    "AND countryCode = :countryCode " +
                    "AND otp = :otp";
            try {
            int updated = session
                    .createQuery(hql)
                    .setParameter("mobile", mobile)
                    .setParameter("countryCode", countryCode.replace("+", ""))
                    .setParameter("otp", otp)
                    .executeUpdate();
            tx.commit();   // 🔥 MUST
            System.out.println(mobile + countryCode + otp + " markVerified updated rows: " + updated);
            
            
            }catch(Exception e) {
            	e.printStackTrace();
            }
//            if (updated == 0) {
//                tx.rollback();   // optional but good
//                throw new OtpException("OTP not found or already used");
//            }

            mcc.delete(getKey(countryCode, mobile));

        } catch (Exception e) {
        	e.printStackTrace();
            if (tx != null) tx.rollback();   // 🔥 IMPORTANT
            throw new OtpException("Failed to mark OTP verified", e);
        }
    }
}