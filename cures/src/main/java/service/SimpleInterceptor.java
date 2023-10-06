package service;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import util.HibernateUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       return true;
       
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // This method is called after the handler method is invoked (after the API call).
        // You can perform additional actions here if needed.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // This method is called after the response has been sent to the client.
        // You can perform additional actions here if needed.
	System.out.println("afterCompletion");
    	String customData = (String) request.getAttribute("customData");
    	if(customData==null)
    	{
    //		System.out.println(customData);
    	}
    	else if(customData=="All Ads are Served")
    	{
  //  		System.out.println(customData);
    	}
    	if(customData!=null && customData!="All Ads are Served" )
    	{
    		store(customData);
        	
    	}
    		
    }
    
    public void store(String URL)
    {
    	Session session = HibernateUtil.buildSessionFactory();
    	session.beginTransaction();
    	int res=0;
    	Integer AdID = null;
	Integer CampaignAdID=null;
	    System.out.println("Current Date in Milliseconds: before select " + System.currentTimeMillis());
    	Query query1 = session.createNativeQuery(
		"Select stats.AdID ID1, c.AdID  ID2 from AdsStats stats inner join CampaignAds  c where  stats.AdID=c.AdID and c.ImageLocation= '" + URL + "';");
    	List<Object[]> results = (List<Object[]>) query1.getResultList();
		System.out.println("Current Date in Milliseconds: after select " + System.currentTimeMillis());
		for (Object[] objects : results) {
			AdID = (Integer) objects[0];
	//		System.out.println(AdID);
		}
		
		if (AdID == null)
		{
			System.out.println("Current Date in Milliseconds: before insert " + System.currentTimeMillis());
			Query query = session.createNativeQuery(
	    			"INSERT INTO AdsStats (AdID, Impressions) SELECT c.AdID, 1 FROM CampaignAds c WHERE c.ImageLocation = '" + URL + "';");
	   // 		System.out.println(query);
	    	try {
			System.out.println("Current Date in Milliseconds: before execution of insert " + System.currentTimeMillis());
				int ret = query.executeUpdate();				
				System.out.println(ret);
				session.getTransaction().commit();
			System.out.println("Current Date in Milliseconds: after execution of insert " + System.currentTimeMillis());
	//			System.out.println(" entry for URL =  " + URL);
				update(URL);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Current Date in Milliseconds: before execution of update " + System.currentTimeMillis());
			Query query = session.createNativeQuery(
	    			"UPDATE AdsStats\r\n"
	    			+ "JOIN CampaignAds ON AdsStats.AdID = CampaignAds.AdID\r\n"
	    			+ "SET AdsStats.Impressions =AdsStats.Impressions + 1\r\n"
	    			+ "WHERE CampaignAds.ImageLocation = '" + URL + "';");
	    		
	    	try {
				int ret = query.executeUpdate();
			
				System.out.println(ret);
				session.getTransaction().commit();
			System.out.println("Current Date in Milliseconds: after execution of update " + System.currentTimeMillis());
	//			System.out.println(" entry for URL =  " + image[1]);
				update(URL);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
	public void update(String URL)
    {
    	Session session1 = HibernateUtil.buildSessionFactory();
    	session1.beginTransaction();
	    System.out.println("Current Date in Milliseconds: before execution of update CampaignAds" + System.currentTimeMillis());
    	Query query = session1.createNativeQuery(
   			"Update CampaignAds set AdDelivered=AdDelivered + 1 WHERE ImageLocation = '" + URL + "';");
    	try {
			int ret = query.executeUpdate();
			System.out.println(ret);
			session1.getTransaction().commit();
		System.out.println("Current Date in Milliseconds: after execution of update CampaignAds" + System.currentTimeMillis());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
