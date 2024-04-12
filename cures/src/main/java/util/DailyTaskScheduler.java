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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

public class DailyTaskScheduler {
	public static MemcachedClient mcc = null;
	private static final String BANNER_KEY_LIST_KEY = "BannerkeyList";
	private static final String LEFT_KEY_LIST_KEY = "LeftkeyList";
	// Key to store the list of keys
	 static int BannerrotationCount1=0;
	 static int LeftrotationCount1=0;
	 
    public static void main(String[] args) throws JsonProcessingException {    	
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long initialDelay = calculateInitialDelay(); // Calculate the initial delay until midnight
       long period = TimeUnit.DAYS.toSeconds(1); // Run every 24 hours

        scheduler.scheduleAtFixedRate(() -> {
            // Perform your daily calculations and update cache here
            performDailyCalculationsAndCacheUpdate();
            try {
				TargetAds.update();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				DisplayPattern();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }, initialDelay, period, TimeUnit.SECONDS);
            
            
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
				"SELECT AdID,ImageLocation, StartDate, EndDate,AdCount,(AdCount-AdDelivered), SlotName\r\n"
				+ "FROM CampaignAds c join AdsSlotTypes t\r\n"
				+ "WHERE CURDATE() BETWEEN StartDate AND EndDate and ReviewStatus=1 and c.SlotID=t.SlotID and DiseaseCondition=0 ");
		List<Object[]> results = (List<Object[]>) query.getResultList();


		List<Object[]> arrayDataList = new ArrayList<>();
		for (Object[] objects : results) {
		    Integer AdID = (Integer) objects[0];
		    
		    String ImageLocation = (String) objects[1];
		    Date StartDate=(Date)objects[2];
		    Date EndDate=(Date)objects[3];
		   Integer Count = (Integer) objects[4];
		   BigInteger AdCount=(BigInteger) objects[5] != null ? (BigInteger) objects[5] : new BigInteger(Count.toString());
		   String AdType=(String)objects[6];
		    Object[] dataArray = new Object[]{AdID, ImageLocation,StartDate,EndDate,AdCount};
		    arrayDataList.add(dataArray);
		    adCountPerDay = calculateAdCountPerDay(arrayDataList);
		   
		    updateMemCache(AdID,ImageLocation,adCountPerDay,AdType);
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
   //         System.out.println(startDate);
           
             endDate = (Date) adData[3];
   //          System.out.println(endDate);
             adCount= ((BigInteger) adData[4]).intValueExact();;
    	}
    	LocalDate localDate=LocalDate.now();
    	Date Date = convertLocalDateToDate(localDate);
 //   	System.out.println(Date);
        long timeDiff = Math.abs(endDate.getTime() - Date.getTime());
        int daysDiff = (int) Math.ceil(timeDiff / (1000.0 * 60 * 60 * 24)) + 1;
   //     System.out.println("Days" + daysDiff);
        int adCountPerDay = (int) Math.ceil(adCount / (double) daysDiff);
        
//        System.out.println("DailyCount" + adCountPerDay);
        return adCountPerDay;
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private static void updateMemCache(Integer  adId, String imageLocation, Integer adCount, String AdType) {
        // Update Memcached with the calculated adCountPerDay value
        // ...
    	if (mcc == null) {
			initializeCacheClient();
		}
    	
    	String AdID =AdType+"_"+adId.toString();
   // 	System.out.println(AdType);
    	String value = imageLocation + ":" + adCount;

        // Store and track keys in Memcached
        storeAndTrackKey(mcc, AdID, value,AdType);
       
        }
    	
    private static void storeAndTrackKey(MemcachedClient memcachedClient, String key, String value,String AdType) {
        // Store the data in Memcached
    	
        
        if (AdType.equalsIgnoreCase("Banner"))
        {
 //       	System.out.println(key + value);
        	memcachedClient.set(key, 0, value);
        	// Append the key to the list
            @SuppressWarnings("unchecked")
			Set<String> keyList = (Set<String>) memcachedClient.get(BANNER_KEY_LIST_KEY);
            if (keyList == null) {
                keyList = new HashSet<>();
            }
            else
            {
            for (String Storedkey : keyList) {
    //            System.out.println("Key: " + Storedkey);
            }
            }
            keyList.add(key);
            memcachedClient.set(BANNER_KEY_LIST_KEY, 0, keyList);
        }
        
        else  if (AdType.equalsIgnoreCase("Left"))
        {
  //      	System.out.println(key + value);
        	
        	memcachedClient.set(key, 0, value);
        	// Append the key to the list
            Set<String> keyList = (Set<String>) memcachedClient.get(LEFT_KEY_LIST_KEY);
            if (keyList == null) {
                keyList = new HashSet<>();
            }
            else
            {
            for (String Storedkey : keyList) {
  //              System.out.println("Key: " + Storedkey);
            }
            }
           
            keyList.add(key);
            memcachedClient.set(LEFT_KEY_LIST_KEY, 0, keyList);
 //           System.out.println("Left:Value " + memcachedClient.get(LEFT_KEY_LIST_KEY));
        }
        
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
    	
    	 LinkedHashMap<String, Integer> BannerAdCount = new LinkedHashMap<>();
    	 LinkedHashMap<String, String> BannerAdURL = new LinkedHashMap<>();
    	 
    	 LinkedHashMap<String, Integer> LeftAdCount = new LinkedHashMap<>();
    	 LinkedHashMap<String, String> LeftAdURL = new LinkedHashMap<>();
    	 
    	 LinkedHashMap<String, Integer> RightAdCount = new LinkedHashMap<>();
    	 LinkedHashMap<String, String> RightAdURL = new LinkedHashMap<>();
    	 
    	 
    	
    	 if (mcc == null) {
 			initializeCacheClient();
 		}
     	
 		// Print the list of tracked keys
 		 Set<String> keyList = (Set<String>) mcc.get(BANNER_KEY_LIST_KEY);
 	        if (keyList != null) {
 	 //           System.out.println("List of Keys: " + keyList);
 	            for (String key : keyList) {
 	//                System.out.println(key);
 	                
 	           // Retrieve ad count from Memcached
 	                String storedValue = (String) mcc.get(key);
 	                
 	                if (storedValue != null) {
 	                    String[] parts = storedValue.split(":");
 	                    if (parts.length == 2) {
 	                        String storedImageLocation = parts[0];
 	                        int storedAdCount = Integer.parseInt(parts[1]);
 	              //         System.out.println("Stored Image Location: " + storedImageLocation);
 	              //         System.out.println("Stored Ad Count: " + storedAdCount);
 	                      BannerrotationCount1= BannerrotationCount1 + storedAdCount;
 	                        BannerAdCount.put(key,storedAdCount);
 	                       
 	                    BannerAdURL.put(key,storedImageLocation );

 	                    }
 	                 } else {
 	      //              System.out.println("Ad not found in Memcached.");
 	                   
 	                }
 	                 	            
 	        } createPattern(BannerAdCount,BannerAdURL,BannerrotationCount1,BANNER_KEY_LIST_KEY);
}
 	        // Print the list of tracked keys
 	   		 Set<String> keyList_LEFT = (Set<String>) mcc.get(LEFT_KEY_LIST_KEY);
 	   	        if (keyList_LEFT != null) {
 	//   	            System.out.println("List of Keys: " + keyList_LEFT);
 	   	            for (String key : keyList_LEFT) {
 	   	         //       System.out.println(key);
 	   	                
	            	// Retrieve ad count from Memcached
	                String storedValue = (String) mcc.get(key);
	                
	                if (storedValue != null) {
	                    String[] parts = storedValue.split(":");
	                    if (parts.length == 2) {
	                        String storedImageLocation = parts[0];
	                        int storedAdCount = Integer.parseInt(parts[1]);
	   //                    System.out.println("Stored Image Location: " + storedImageLocation);
	  //                     System.out.println("Stored Ad Count: " + storedAdCount);
	                      LeftrotationCount1= LeftrotationCount1 + storedAdCount;
	                        LeftAdCount.put(key,storedAdCount);
	    //                   System.out.println("LeftAdCount" + LeftAdCount);
	                    LeftAdURL.put(key,storedImageLocation );

	                    }
	                 } else {
	    //                System.out.println("Ad not found in Memcached.");
	                   
	                }
	            
 	   	            }createPattern(LeftAdCount,LeftAdURL,LeftrotationCount1,LEFT_KEY_LIST_KEY);}
 	             else {
 	//            System.out.println("No keys found in the list.");
 	            
 	        }  
 	            }
 	             
 	             
 	        
 	     
 /*	      AdCount.put("9", 15);
 	     AdCount.put("13", 45);
     AdCount.put("11", 75);
 //	   AdCount.put("12", 165);
 	   AdURL.put("9","/home/uat/Production/installers/tomcat/webapps/cures_adsimages/Ad_9.jpg" );
 	  AdURL.put("13","/home/uat/Production/installers/tomcat/webapps/cures_adsimages/Ad_13.jpg" );
	 AdURL.put("11","/home/uat/Production/installers/tomcat/webapps/cures_adsimages/Ad_15.jpg" );
 //	AdURL.put("12","/home/uat/Production/installers/tomcat/webapps/cures_adsimages/Ad_12.jpg" );
 	*/
 	
            
    

    
    static void createPattern( LinkedHashMap<String, Integer> AdCount,LinkedHashMap<String, String> AdURL, Integer rotationCount1 , String KEY_LIST_KEY) throws JsonProcessingException
    {
    	int count =0;
    	 Set<String> keyList = (Set<String>) mcc.get(KEY_LIST_KEY);
//    	 System.out.println("keyList" + keyList);
    	  List<String> ads = new ArrayList<>();
//    	  System.out.println(AdCount);
//          System.out.println(AdURL);
     	   
      	       if(AdCount.size() ==1)
      	       {
   //   	    	   System.out.println("Single AD");
      	    	 for ( String key1 : keyList) {
    //  	    		System.out.println(AdCount.get(key1));
      	    		 count=AdCount.get(key1);
      	    	 
      	    		 for (int i = 0; i < count; i++) {
      	    			ads.add(key1);
      	    		 }}
      	       }
      	       
      	       
      	       else {
   //   	    	 System.out.println("AdCount::"+AdCount);
      	    	 LinkedHashMap<String, Double> Result= SimplifiedRatioCalculator.CalculateRatio(AdCount);
  //  	           System.out.println("Size:"+AdCount.size());  
    	            ads =DynamicAdPattern.generateAds(Result);
    	  
      	       }
      	         	     
 //           System.out.println("ADS:"+ads);
 //           System.out.println(rotationCount1);
            int DC_Cond=0;
           displayRotatedAds(ads, rotationCount1,AdCount,AdURL,DC_Cond);
    	
    }
	static void displayRotatedAds(List<String> ads, int rotationCount, Map<String, Integer> brandSkipCounts,LinkedHashMap<String, String> AdURL, Integer DC_Cond) throws JsonProcessingException {
       if(DC_Cond!=0)
       {
    	   String result="";
    	   int totalAds = ads.size();
           String match=ads.get(0);
           
           String[] parts = match.split("_");
           if (parts.length >=2) {
               result = parts[0] + "_" + parts[1] + "_"; // Concatenate the first two parts
  //            System.out.println("Result: " + result);
          } 
          if (mcc == null) {
	 initializeCacheClient();
	 }
           int adIndex = 0;
           int index=0;
           
           for (int i = 0; i < rotationCount; i++) {
               String currentAd = ads.get(adIndex);
               // Check if the current AD should be skipped
               if (brandSkipCounts.containsKey(currentAd) && brandSkipCounts.get(currentAd) > 0) {
               	
               	brandSkipCounts.put(currentAd, brandSkipCounts.get(currentAd) - 1);
   //            	System.out.println(currentAd); 
               	String URL=AdURL.get(currentAd);
               	
   //            	String key="Banner" + String.valueOf(index);
               	String key=result + String.valueOf(index);
   //            	System.out.println("Key:" + key);
   //            	System.out.println("URL:" + URL);
               	index=index+1;
               	
               	// Append the URL to the list
                 mcc.set(key,0,URL );
   //             System.out.println("in memcached" + (String)mcc.get(key)); 
               	
       //        } else {
               
       //       	rotationCount=rotationCount +1;
               	
       //        }
               adIndex = (adIndex + 1) % totalAds;
           }

       }
       }
		
       else
       {
    	   String result="";
    	   int totalAds = ads.size();
           String match=ads.get(0);
           
           String[] parts = match.split("_");
           
           int adIndex = 0;
           int index=0;
           
           for (int i = 0; i < rotationCount; i++) {
               String currentAd = ads.get(adIndex);
               // Check if the current AD should be skipped
               if (brandSkipCounts.containsKey(currentAd) && brandSkipCounts.get(currentAd) > 0) {

               	brandSkipCounts.put(currentAd, brandSkipCounts.get(currentAd) - 1);
  //             	System.out.println(currentAd); 
               	String URL=AdURL.get(currentAd);
               	
   //            	String key="Banner" + String.valueOf(index);
               	String key=parts[0] + "_0_"  + String.valueOf(index) ;
    //           	System.out.println("Key:" + key);
  //             	System.out.println("URL:" + URL);
               	index=index+1;
               	
               	// Append the URL to the list
                 mcc.set(key,0,URL );
             
               	
       //        } else {
               
       //       	rotationCount=rotationCount +1;
               	
       //        }
               adIndex = (adIndex + 1) % totalAds;
           }

       }
           
       }
		
        
       
  //      if(ads.stream().anyMatch(ad -> ad.contains("Banner")))
  //      {
       
        	
        
    /*    if(ads.stream().anyMatch(ad -> ad.contains("Left")))
        {
        	int adIndex = 0;
            int index=0;
        
            for (int i = 0; i < rotationCount; i++) {
                String currentAd = ads.get(adIndex);
                // Check if the current AD should be skipped
                if (brandSkipCounts.containsKey(currentAd) && brandSkipCounts.get(currentAd) > 0) {
                	
                	brandSkipCounts.put(currentAd, brandSkipCounts.get(currentAd) - 1);
                	
                	String URL=AdURL.get(currentAd);
                	String key="Left" + String.valueOf(index);
                	index=index+1;
                	 
                	// Append the URL to the list
                  mcc.set(key,0,URL );  	
                } else {
         //       	System.out.println("hh");
        //       	rotationCount=rotationCount +1;
                	
                }
                adIndex = (adIndex + 1) % totalAds;
            }

        }
       */ 	
        	
    }   
  
}
