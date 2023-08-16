package service;


import org.hibernate.Session;
import org.hibernate.query.Query;
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
    	String customData = (String) request.getAttribute("customData");
    	if(customData==null)
    	{
    		System.out.println(customData);
    	}
    	else if(customData!="All Ads are Served")
    	{
    		System.out.println(customData);
    	}
    	if(customData!=null )
    	{
    		store(customData);
        	
    	}
    		
    }
    
    public void store(String URL)
    {
    	Session session = HibernateUtil.buildSessionFactory();
    	session.beginTransaction();
    	int res=0;
	String substring = "444";
	String[] image = URL.split(substring, 2); 
	System.out.println(image[1]);
    	Integer AdID = null;
    	Query query1 = session.createNativeQuery(
				"Select stats.AdID ID1, c.AdID  ID2 from AdsStats stats inner join CampaignAds  c where  stats.AdID=c.AdID and c.ImageLocation= '" + image[1] + "';");
    	List<Object[]> results = (List<Object[]>) query1.getResultList();
		
		for (Object[] objects : results) {
		
			AdID = (Integer) objects[0];
			System.out.println(AdID);
		}
		
		if (AdID == null)
		{
			Query query = session.createNativeQuery(
	    			"INSERT INTO AdsStats (AdID, Impressions) SELECT c.AdID, 1 FROM CampaignAds c WHERE c.ImageLocation = '" + image[1] + "';");
	    		System.out.println(query);
	    	try {
				int ret = query.executeUpdate();
				System.out.println(ret);
				session.getTransaction().commit();
				System.out.println(" entry for URL =  " + URL);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Query query = session.createNativeQuery(
	    			"UPDATE AdsStats\r\n"
	    			+ "JOIN CampaignAds ON AdsStats.AdID = CampaignAds.AdID\r\n"
	    			+ "SET AdsStats.Impressions =AdsStats.Impressions + 1\r\n"
	    			+ "WHERE CampaignAds.ImageLocation = '" + image[1] + "';");
	    		
	    	try {
				int ret = query.executeUpdate();
				System.out.println(ret);
				session.getTransaction().commit();
				System.out.println(" entry for URL =  " + image[1]);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
}
