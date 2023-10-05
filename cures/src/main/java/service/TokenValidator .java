package service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class TokenValidator {

    public static int isValidToken(String token, String url) {
    	
    	 if (token.startsWith("Bearer ")) {
  	        token = token.substring("Bearer ".length());
  	    }
    	 
    	 System.out.println("token" + token);
 		Session session = HibernateUtil.buildSessionFactory();
 		int res=0;
 		Date current_Date= generating_Current_Date();
 		 Query query = session.createNativeQuery("SELECT\r\n"
 		 		+ "    at.TokenID,\r\n"
 		 		+ "    at.Token,\r\n"
 		 		+ "    at.Status,\r\n"
 		 		+ "    at.Max_Allowed,\r\n"
 		 		+ "    aa.Total_Count,\r\n"
 		 		+ "    aa.LastUpdateDate\r\n"
 		 		+ "FROM\r\n"
 		 		+ "    API_Tokens at\r\n"
 		 		+ "JOIN\r\n"
 		 		+ "    APITokenAnalytics aa\r\n"
 		 		+ "ON\r\n"
 		 		+ "    at.TokenID = aa.TokenID\r\n"
 		 		+ "where Token =   '"+token+"' and aa.API='"+url+ "' \r\n"
 		 		+ ";");
 		 try {
 			 List<Object[]> results =(List<Object[]>)  query.getResultList();
 			 
 			 if(results.size()>0) {
 				 
 				 for(Object[] list:results) {
 					 
 					Integer TokenID= (Integer) list[0];
 					String Token=(String) list[1];
 					Integer Status=(Integer) list[2];
 					Integer Max_Allowed=(Integer) list[3]==null?0:(Integer) list[3];
 					Integer Total_Count=(Integer) list[4]==null?0:(Integer) list[4];
 					Date LastUpdateDate=(Date) list[5];
 					 
 					// 1 for update
 					// 2 for increment
 					
 					if(token.equals(Token) && Status == 1) {
 						if(Max_Allowed==Total_Count &&  false == current_Date.equals(LastUpdateDate)) {
 							res=update_Total_Count(url,TokenID,Total_Count, 1);
 							
 						}
 						if(Max_Allowed==Total_Count && true == current_Date.equals(LastUpdateDate)) {
 							res=2;
 						}
 						if(Max_Allowed == 0) {
 							res=1;
 						}
 						if(Max_Allowed > Total_Count) {
 							res = update_Total_Count(url,TokenID,Total_Count, 2);
 							
 						}
 					}
 					
 				 }
 			 }
 			 
 			  
 		 } catch (NoResultException e) {
 				System.out.println("No Entry");

 			}
 		 System.out.println("res"+res);
 		 return res;
    }
    
    public static int update_Total_Count(String url,Integer tokenID,Integer Total_count, int toDo) {
		Session session = HibernateUtil.buildSessionFactory();
	      session.beginTransaction();
	      int ret=0;  
	      String str = "";
	      if(Total_count ==0) {
	    	  str="UPDATE APITokenAnalytics\r\n"
	    	  		+ "SET Total_Count = 1\r\n"
	    	  		+ "where TokenID="+tokenID+" and API = '"+url+"'\r\n"
	    	  		+ ";";
	    	  toDo=0;
	      }
	      if(toDo ==1) {
	    	  str="UPDATE APITokenAnalytics\r\n"
	    	  		+ "SET Total_Count = 1\r\n"
	    	  		+ "where TokenID="+tokenID+" and API = '"+url+"'\r\n"
	    	  		+ ";";
	      }
	      if(toDo==2) {
	    	  str="UPDATE APITokenAnalytics\r\n"
	    	  		+ "SET Total_Count = Total_Count+1\r\n"
	    	  		+ "where TokenID="+tokenID+" and API = '"+url+"'\r\n"
	    	  		+ ";";
	      }
	       
	      try {
	    	  Query query = session.createNativeQuery(str);
	      ret = query.executeUpdate();
	      
	      session.getTransaction().commit();
	       
	     
//	      System.out.println(ret);
	      }catch (Exception e) {
				e.printStackTrace();
				session.getTransaction().rollback();
			}
		
		
		return ret;
	}
	
	
	public static Date generating_Current_Date() {
		LocalDate currentDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = currentDate.atStartOfDay(zoneId);
        Date nowDate = Date.from(zonedDateTime.toInstant());
		return nowDate;
	}
}
