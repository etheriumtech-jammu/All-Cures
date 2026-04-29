package service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.hibernate.Session;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import exception.OtpException;
import model.OtpEntity;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.HibernateUtil;

@Service
@Transactional
public class OtpStorageService {

	   Session session = HibernateUtil.buildSessionFactory();
	   public static MemcachedClient mcc = null;
    private static final int OTP_EXPIRY = 5; // minutes
    
    //Key format: countryCode:mobile
    private String getKey(String countryCode, String mobile) {
    	 // clean inputs
        String cleanCountryCode = countryCode
                .replace("+", "")
                .replaceAll("[^0-9]", ""); // only digits

        String cleanMobile = mobile
                .replaceAll("[^0-9]", ""); // only digits

        return cleanCountryCode + ":" + cleanMobile;
    }
    
    public static MemcachedClient initializeCacheClient() {
		try {
			Constant.log("Trying Connection to Memcache server", 0);
			mcc = new MemcachedClient(
					new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
					AddrUtil.getAddresses(Constant.ADDRESS));
			Constant.log("Connection to Memcache server Sucessful", 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constant.log("Connection to Memcache server UN-Sucessful", 3);
		}
		return mcc;
	}
    public void saveOtp(String countryCode,String mobile, String otp) {
    	if (mcc == null) {
			initializeCacheClient();
		}

    	 try {
            OtpEntity entity = new OtpEntity();
            entity.setOtp(otp);
            entity.setCountryCode(countryCode.trim().replace("+", ""));
            entity.setMobile(mobile.trim()); 
            entity.setStatus("SENT");
            entity.setCreatedAt(java.time.LocalDateTime.now());
            entity.setExpiryTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(OTP_EXPIRY));
             session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();

            
            String key = getKey(countryCode, mobile);
            System.out.println("Generated Memcached Key: [" + key + "]");
            mcc.set(key, OTP_EXPIRY * 60, otp);

        } catch (Exception e) {
        	if(session.getTransaction() != null) {
            	session.getTransaction().rollback();
            	}
            throw new OtpException("Failed to save OTP", e);
        }
    }

    public String getOtp(String countryCode,String mobile) {
    	if (mcc == null) {
			initializeCacheClient();
		}
    	 String key = getKey(countryCode, mobile);
         Object cachedOtp = mcc.get(key);

         if (cachedOtp != null) {
             System.out.println("OTP fetched from cache");
             return cachedOtp.toString();
         }
    	   Session session = HibernateUtil.buildSessionFactory();
            try {
            String hql = "FROM OtpEntity " +
                         "WHERE mobile = :mobile " +
                         "AND countryCode = :countryCode " +
                         "AND expiryTime > :currentTime " +
                         "ORDER BY createdAt DESC";

            OtpEntity entity = (OtpEntity) session
                    .createQuery(hql)
                    .setParameter("mobile", mobile)
                    .setParameter("countryCode", countryCode.trim().replace("+", ""))
                    .setParameter("currentTime", System.currentTimeMillis())
                    .setMaxResults(1)
                    .uniqueResult();

            if (entity == null) {
                System.out.println("No valid OTP found");
                return null;
            }
            mcc.set(key, OTP_EXPIRY * 60, entity.getOtp());

            return entity.getOtp();

        } catch (Exception e) {
            throw new OtpException("Failed to fetch OTP", e);
        }
    }
    public void deleteOtp(String mobile, String countryCode) {
    	  
             try {
            String hql = "DELETE FROM OtpEntity WHERE mobile = :mobile AND countryCode = :countryCode";
            session.beginTransaction();
            session
                    .createQuery(hql)
                    .setParameter("mobile", mobile)
                    .setParameter("countryCode",countryCode)
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("OTP deleted successfully");
        } catch (Exception e) {
        	if(session.getTransaction() != null) {
        	session.getTransaction().rollback();
        	}
        	System.out.println("Failed to delete OTP: " + e.getMessage());
            throw new OtpException("Failed to delete OTP", e);
        }
    }

    public void markVerified( String countryCode,String mobile, String otp) {
    	if (mcc == null) {
			initializeCacheClient();
		}
                
        try {
            String hql = "UPDATE OtpEntity SET status='VERIFIED' WHERE mobile = :mobile AND countryCode = :countryCode AND otp = :otp";
             session.beginTransaction();
             int updatedRows = session
            	        .createQuery(hql)
            	        .setParameter("mobile", mobile)
            	        .setParameter("countryCode", countryCode.trim().replace("+", ""))
            	        .setParameter("otp", otp)
            	        .executeUpdate();

            	System.out.println("Rows updated: " + updatedRows);
             session.getTransaction().commit();
             mcc.delete(getKey(countryCode, mobile));
             System.out.println("OTP marked as VERIFIED successfully");
        } catch (Exception e) {
        	if(session.getTransaction() != null) {
            	session.getTransaction().rollback();
            	}
        	 System.out.println("Failed to update OTP status: " + e.getMessage());
            throw new OtpException("Failed to update OTP status", e);
        }
    }
}
