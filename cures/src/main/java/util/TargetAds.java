package util;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

public class TargetAds {

	public static MemcachedClient mcc = null;
	public static LinkedHashMap<String, String> DC_AdCount = new LinkedHashMap<>();
	public static void main(String[] args) throws JsonProcessingException
	{
	   HashMap<String, ArrayList<Integer>> adsByCondition = new HashMap<>();
	   List<String> dcCondList = new ArrayList<>();
	   Integer DC_cond=0;
	   LinkedHashMap<Integer, Integer> D_AdCount = new LinkedHashMap<>();
	   LinkedHashMap<String, String> AdURL = new LinkedHashMap<>();
	   Set<String> dcCondSet = null;
	   int rotationCount=0;
		Session session = HibernateUtil.buildSessionFactory();
  	  int adCountPerDay=0;
		Query query = session.createNativeQuery(
				"SELECT AdID,ImageLocation, StartDate, EndDate,AdCount,(AdCount-AdDelivered), AdTypeName,DiseaseCondition\r\n"
				+ "				FROM allcures1.campaignads c join allcures1.adstypes t\r\n"
				+ "				WHERE CURDATE() BETWEEN StartDate AND EndDate and ReviewStatus=1 and c.AdTypeID=t.AdTypeID");
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
		    DC_cond=(Integer) objects[7];
		    Object[] dataArray = new Object[]{AdID, ImageLocation,StartDate,EndDate,AdCount};
		    arrayDataList.add(dataArray);
		    adCountPerDay = calculateAdCountPerDay(arrayDataList);
		    
		   		    D_AdCount.put(AdID,adCountPerDay);
		   		    AdURL.put("Banner_" + DC_cond + "_"+ AdID,ImageLocation);
//		    HashMap<Integer, ArrayList<Integer>> adsByCondition = new HashMap<>();
		    
		        if (!adsByCondition.containsKey("DC_" +DC_cond)) {
		            adsByCondition.put("DC_" + DC_cond, new ArrayList<>());
		        }
		        adsByCondition.get("DC_" + DC_cond).add(AdID);
		       

		        // Iterate through the keys and add them to the List
		        dcCondSet = new HashSet<>(adsByCondition.keySet());
		     // Print D_AdCount, adsByCondition, and dcCondList
		        System.out.println("D_AdCount: " + D_AdCount);
		        System.out.println("adsByCondition: " + adsByCondition);
		        System.out.println("dcCondSet: " + dcCondSet);
		        
		    }
				 HashMap<String, HashMap<Integer, Integer>> resultMap = new HashMap<>();
	
		 dcCondSet = new HashSet<>(adsByCondition.keySet());
		 Iterator<String> iterator = dcCondSet.iterator();
	        while (iterator.hasNext()) {
	            String condition = iterator.next();

	            // Create a new valuesAndCounts map for each condition
	            HashMap<Integer, Integer> valuesAndCounts = new HashMap<>();
	            
	            ArrayList<Integer> adIDs = adsByCondition.get(condition);

	            for (Integer adID : adIDs) {
	                valuesAndCounts.put(adID, D_AdCount.get(adID));
	            }

	            // Store the valuesAndCounts map in resultMap
	            resultMap.put(condition, new HashMap<>(valuesAndCounts));
	            
	        }
	        // Print resultMap
	       
	        
	        for (Map.Entry<String, HashMap<Integer, Integer>> entry : resultMap.entrySet()) {
	            String condition = entry.getKey();
	            Set<String> keyList = new HashSet<>();
	            HashMap<Integer, Integer> valuesCounts = entry.getValue();
	            LinkedHashMap<String, Integer> AdCount = new LinkedHashMap<>();
	            System.out.println("Condition: " + condition);
	            String[] parts = condition.split("_");
	            System.out.println("hhh" + parts[1]);
	            for (HashMap.Entry<Integer, Integer> subEntry : valuesCounts.entrySet()) {
	                Integer adID = subEntry.getKey();
	                Integer count = subEntry.getValue();
	               rotationCount=rotationCount +count;
	               System.out.println("  AdID: " + adID + ", Count: " + count);
	                AdCount.put("Banner_" + parts[1] + "_"+ adID, count);
	                keyList.add("Banner_" + parts[1] + "_"+ adID);
	                System.out.println(AdCount);
	                
	            }
	            System.out.println("KEYLIST"+keyList);
	            List<String> ads = new ArrayList<>();
	            if(AdCount.size() ==1)
	      	       {
	      	    	   System.out.println("Single AD");
	      	    	 for ( String key1 : keyList) {
	      	    		 
	      	    		System.out.println(key1);
	      	    		 Integer count = AdCount.get(key1);
	      	    	 
	      	    		 for (int i = 0; i < count; i++) {
	      	    			ads.add(key1);
	      	    		 }}
	      	       }
	      	       
	            else
	            {
	            LinkedHashMap<String, Double> Result= SimplifiedRatioCalculator.CalculateRatio(AdCount);
		           System.out.println("Size:"+AdCount.size());  
	            
		            ads =DynamicAdPattern.generateAds(Result);
		            System.out.println(ads);
	            }
	            if (mcc == null) {
	     			initializeCacheClient();
	     		}
	            System.out.println("AdURL"+AdURL);
	            DC_AdCount.put(parts[1],rotationCount+ ":0");
	            System.out.println("DC_AdCount"+DC_AdCount);
	            System.out.println("rotationCount"+rotationCount);
		            DailyTaskScheduler.displayRotatedAds(ads, rotationCount,AdCount,AdURL,Integer.parseInt(condition));
	        }
	        
		}
	
	public static void update() throws JsonProcessingException
	{
		HashMap<String, ArrayList<Integer>> adsByCondition = new HashMap<>();
		   List<String> dcCondList = new ArrayList<>();
		   Integer DC_cond=0;
		   LinkedHashMap<Integer, Integer> D_AdCount = new LinkedHashMap<>();
		   LinkedHashMap<String, String> AdURL = new LinkedHashMap<>();
		   Set<String> dcCondSet = null;
		   int rotationCount=0;
			Session session = HibernateUtil.buildSessionFactory();
	  	  int adCountPerDay=0;
			Query query = session.createNativeQuery(
					"SELECT AdID,ImageLocation, StartDate, EndDate,AdCount,(AdCount-AdDelivered), AdTypeName,DiseaseCondition\r\n"
					+ "				FROM CampaignAds c join AdsTypes t\r\n"
					+ "				WHERE CURDATE() BETWEEN StartDate AND EndDate and ReviewStatus=1 and c.AdTypeID=t.AdTypeID and DiseaseCondition!=0");
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
			    DC_cond=(Integer) objects[7];
			    Object[] dataArray = new Object[]{AdID, ImageLocation,StartDate,EndDate,AdCount};
			    arrayDataList.add(dataArray);
			    adCountPerDay = calculateAdCountPerDay(arrayDataList);
			    
			   		    D_AdCount.put(AdID,adCountPerDay);
			   		    AdURL.put("Banner_" + DC_cond + "_"+ AdID,ImageLocation);
//			    HashMap<Integer, ArrayList<Integer>> adsByCondition = new HashMap<>();
			    
			        if (!adsByCondition.containsKey("DC_" +DC_cond)) {
			            adsByCondition.put("DC_" + DC_cond, new ArrayList<>());
			        }
			        adsByCondition.get("DC_" + DC_cond).add(AdID);
			       

			        // Iterate through the keys and add them to the List
			        dcCondSet = new HashSet<>(adsByCondition.keySet());
			     // Print D_AdCount, adsByCondition, and dcCondList
			        System.out.println("D_AdCount: " + D_AdCount);
			        System.out.println("adsByCondition: " + adsByCondition);
			        System.out.println("dcCondSet: " + dcCondSet);
			        
			    }
					 HashMap<String, HashMap<Integer, Integer>> resultMap = new HashMap<>();
		
			 dcCondSet = new HashSet<>(adsByCondition.keySet());
			 Iterator<String> iterator = dcCondSet.iterator();
		        while (iterator.hasNext()) {
		            String condition = iterator.next();

		            // Create a new valuesAndCounts map for each condition
		            HashMap<Integer, Integer> valuesAndCounts = new HashMap<>();
		            
		            ArrayList<Integer> adIDs = adsByCondition.get(condition);

		            for (Integer adID : adIDs) {
		                valuesAndCounts.put(adID, D_AdCount.get(adID));
		            }

		            // Store the valuesAndCounts map in resultMap
		            resultMap.put(condition, new HashMap<>(valuesAndCounts));
		            
		        }
		        // Print resultMap
		       
		        
		        for (Map.Entry<String, HashMap<Integer, Integer>> entry : resultMap.entrySet()) {
		            String condition = entry.getKey();
		            Set<String> keyList = new HashSet<>();
		            HashMap<Integer, Integer> valuesCounts = entry.getValue();
		            LinkedHashMap<String, Integer> AdCount = new LinkedHashMap<>();
		            System.out.println("Condition: " + condition);
		            String[] parts = condition.split("_");
		            System.out.println("hhh" + parts[1]);
		            for (HashMap.Entry<Integer, Integer> subEntry : valuesCounts.entrySet()) {
		                Integer adID = subEntry.getKey();
		                Integer count = subEntry.getValue();
		               rotationCount=rotationCount +count;
		               System.out.println("  AdID: " + adID + ", Count: " + count);
		                AdCount.put("Banner_" + parts[1] + "_"+ adID, count);
		                keyList.add("Banner_" + parts[1] + "_"+ adID);
		                System.out.println(AdCount);
		                
		            }
		            System.out.println("KEYLIST"+keyList);
		            List<String> ads = new ArrayList<>();
		            if(AdCount.size() ==1)
		      	       {
		      	    	   System.out.println("Single AD");
		      	    	 for ( String key1 : keyList) {
		      	    		 
		      	    		System.out.println(key1);
		      	    		 Integer count = AdCount.get(key1);
		      	    		 rotationCount=count;
		      	    		 for (int i = 0; i < count; i++) {
		      	    			ads.add(key1);
		      	    		 }}
		      	       }
		      	       
		            else
		            {
		            LinkedHashMap<String, Double> Result= SimplifiedRatioCalculator.CalculateRatio(AdCount);
			           System.out.println("Size:"+AdCount.size());  
		            
			            ads =DynamicAdPattern.generateAds(Result);
			            System.out.println(ads);
		            }
		            if (mcc == null) {
		     			initializeCacheClient();
		     		}
		            System.out.println("AdURL"+AdURL);
		            DC_AdCount.put(parts[1],rotationCount+ ":0");
		            System.out.println("DC_AdCount"+DC_AdCount);
		            System.out.println("rotationCount"+rotationCount);
		            
			      DailyTaskScheduler.displayRotatedAds(ads, rotationCount,AdCount,AdURL,Integer.parseInt(parts[1]));
		        }
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
        
        System.out.println("DailyCount" + adCountPerDay);
        return adCountPerDay;
    }
	public static Date convertLocalDateToDate(LocalDate localDate) {
		
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
}
