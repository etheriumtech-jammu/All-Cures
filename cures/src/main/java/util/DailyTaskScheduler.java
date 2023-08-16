package util;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

public class DailyTaskScheduler {
	public static MemcachedClient mcc = null;
	private static final String KEY_LIST_KEY = "keyList"; // Key to store the list of keys
	 static int rotationCount1=0;
    public static void main(String[] args) throws JsonProcessingException {    	
   //     ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

   //     long initialDelay = calculateInitialDelay(); // Calculate the initial delay until midnight
  //      long period = TimeUnit.DAYS.toSeconds(1); // Run every 24 hours

  //      scheduler.scheduleAtFixedRate(() -> {
            // Perform your daily calculations and update cache here
            performDailyCalculationsAndCacheUpdate();
            DisplayPattern();
  //      }, initialDelay, period, TimeUnit.SECONDS);
            
            
    }

    private static long calculateInitialDelay() {
        // Calculate the time until the next midnight
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Instant midnightInstant = tomorrow.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
        Duration durationUntilMidnight = Duration.between(Instant.now(), midnightInstant);

        return durationUntilMidnight.getSeconds();
    }
    public static void performDailyCalculationsAndCacheUpdate() {
    	Session session = HibernateUtil.buildSessionFactory();
    	  int adCountPerDay=0;
		Query query = session.createNativeQuery(
				"SELECT AdID,ImageLocation, StartDate, EndDate,AdCount,(AdCount-AdDelivered)\r\n"
				+ "FROM CampaignAds\r\n"
				+ "WHERE CURDATE() BETWEEN StartDate AND EndDate and ReviewStatus=1");
		List<Object[]> results = (List<Object[]>) query.getResultList();


		List<Object[]> arrayDataList = new ArrayList<>();
		for (Object[] objects : results) {
		    Integer AdID = (Integer) objects[0];
		    
		    String ImageLocation = (String) objects[1];
		    Date StartDate=(Date)objects[2];
		    Date EndDate=(Date)objects[3];
		   Integer Count = (Integer) objects[4];
		   BigInteger AdCount=(BigInteger) objects[5] != null ? (BigInteger) objects[5] : new BigInteger(Count.toString());
		   
		    Object[] dataArray = new Object[]{AdID, ImageLocation,StartDate,EndDate,AdCount};
		    arrayDataList.add(dataArray);
		    adCountPerDay = calculateAdCountPerDay(arrayDataList);
		   
		    updateMemCache(AdID,ImageLocation,adCountPerDay);
		}
        // Calculate and update Redis cache with the calculated data
        
       
    }
  
    public static int calculateAdCountPerDay(List<Object[]> ad) {
    	
    	Date startDate = null ;
    	Date endDate = null;
    	Integer adCount = null;
    	for (Object[] adData : ad) {
            // Assuming the adData array has specific indexes for start date, end date, and ad count
    		Integer AdID=(Integer) adData[0];
    		
            startDate = (Date) adData[2];
            System.out.println(startDate);
           
             endDate = (Date) adData[3];
             System.out.println(endDate);
             adCount= ((BigInteger) adData[4]).intValueExact();;
    	}
    	LocalDate localDate=LocalDate.now();
    	Date Date = convertLocalDateToDate(localDate);
    	System.out.println(Date);
        long timeDiff = Math.abs(endDate.getTime() - Date.getTime());
        int daysDiff = (int) Math.ceil(timeDiff / (1000.0 * 60 * 60 * 24)) + 1;
        System.out.println("Days" + daysDiff);
        int adCountPerDay = (int) Math.ceil(adCount / (double) daysDiff);
        
        System.out.println("DailyCount" + adCountPerDay);
        return adCountPerDay;
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private static void updateMemCache(Integer  adId, String imageLocation, Integer adCount) {
        // Update Memcached with the calculated adCountPerDay value
        // ...
    	if (mcc == null) {
			initializeCacheClient();
		}
    	
    	String AdID = adId.toString();       
    	String value = imageLocation + ":" + adCount;

        // Store and track keys in Memcached
        storeAndTrackKey(mcc, AdID, value);
       
        }
    	
    private static void storeAndTrackKey(MemcachedClient memcachedClient, String key, String value) {
        // Store the data in Memcached
        memcachedClient.set(key, 0, value);
        
        // Append the key to the list
        Set<String> keyList = (Set<String>) memcachedClient.get(KEY_LIST_KEY);
        if (keyList == null) {
            keyList = new HashSet<>();
        }
        else
        {
        for (String Storedkey : keyList) {
            System.out.println("Key: " + Storedkey);
        }
        }
       
        keyList.add(key);
        memcachedClient.set(KEY_LIST_KEY, 0, keyList);
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
    public static void DisplayPattern() throws JsonProcessingException
    {
    	int count =0;
    	 LinkedHashMap<String, Integer> AdCount = new LinkedHashMap<>();
    	 LinkedHashMap<String, String> AdURL = new LinkedHashMap<>();
    	
    	 if (mcc == null) {
 			initializeCacheClient();
 		}
     	
 		// Print the list of tracked keys
 		 Set<String> keyList = (Set<String>) mcc.get(KEY_LIST_KEY);
 	        if (keyList != null) {
 	            System.out.println("List of Keys: " + keyList);
 	            for (String key : keyList) {
 	                System.out.println(key);
 	            
 	             // Retrieve ad count from Memcached
 	                String storedValue = (String) mcc.get(key);
 	                if (storedValue != null) {
 	                    String[] parts = storedValue.split(":");
 	                    if (parts.length == 2) {
 	                        String storedImageLocation = parts[0];
 	                        int storedAdCount = Integer.parseInt(parts[1]);
 	                       System.out.println("Stored Image Location: " + storedImageLocation);
 	                       System.out.println("Stored Ad Count: " + storedAdCount);
 	                       rotationCount1= rotationCount1 + storedAdCount;
 	                          AdCount.put(key,storedAdCount);
 	                       
 	                      AdURL.put(key,storedImageLocation );

 	                    }
 	                 List<String> ads = new ArrayList<>();
 	                
 	     	       if(AdCount.size() ==1)
 	     	       {
 	     	    	 for (String key1 : keyList) {
 	     	    		 count=AdCount.get(key);
 	     	    	 }
 	     	    		 for (int i = 0; i < count; i++) {
 	     	    			ads.add(key1);
 	     	    		 }
 	     	       }
 	     	       else {
 	     	    	 LinkedHashMap<String, Double> Result= SimplifiedRatioCalculator.CalculateRatio(AdCount);

 	  	           System.out.println("Size:"+AdCount.size());  
 	  	            ads =DynamicAdPattern.generateAds(Result);
 	  	  
 	     	       }
 	    	        
 	          
 	           System.out.println("ADS:"+ads);
 	           System.out.println(rotationCount1);
 	          displayRotatedAds(ads, rotationCount1,AdCount,AdURL);
 	                } else {
 	                    System.out.println("Ad not found in Memcached.");
 	                }
 	            }
 	        } else {
 	            System.out.println("No keys found in the list.");
 	        } 
    } 
    @SuppressWarnings("unchecked")
	static void displayRotatedAds(List<String> ads, int rotationCount, Map<String, Integer> brandSkipCounts,LinkedHashMap<String, String> AdURL) throws JsonProcessingException {
        int totalAds = ads.size();
        int adIndex = 0;
        int index=0;
        System.out.println(rotationCount);
        for (int i = 0; i < rotationCount; i++) {
            String currentAd = ads.get(adIndex);
            // Check if the current AD should be skipped
            if (brandSkipCounts.containsKey(currentAd) && brandSkipCounts.get(currentAd) > 0) {
            	brandSkipCounts.put(currentAd, brandSkipCounts.get(currentAd) - 1);
            	System.out.println(currentAd); 
            	String URL=AdURL.get(currentAd);
            	String key=String.valueOf(index);
            	index=index+1;
            	
            	// Append the URL to the list
              mcc.set(key,0,URL );
             System.out.println("in memcached" + (String)mcc.get(key)); 
            } else {
            	
            	rotationCount=rotationCount +1;
            }
            adIndex = (adIndex + 1) % totalAds;
        }
	
    }   
  
}
