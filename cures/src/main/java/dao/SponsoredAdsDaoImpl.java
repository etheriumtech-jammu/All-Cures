package dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.commons.io.FilenameUtils;
import javax.persistence.NoResultException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import org.hibernate.Transaction;
import util.Constant;
import util.HibernateUtil;
import java.util.concurrent.ConcurrentHashMap;
import util.DailyTaskScheduler;
import util.TargetAds;
import org.springframework.scheduling.annotation.Async;
import model.SponsoredServicesMaster;
import model.ServiceContract;
public class SponsoredAdsDaoImpl {

	public static Set<String> keySet = new HashSet<>();
	public static MemcachedClient mcc = null;
	private static Map<LocalDate, Integer> BannerCountMap = new ConcurrentHashMap<>();
	    private static Map<LocalDate, Integer> LeftCountMap = new ConcurrentHashMap<>();
	    private static LocalDate lastRequestDate = null;
	public static Integer InsertCompaniesDetails( HashMap<String, Object> companyMap) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();
		Date date= Date.valueOf(LocalDate.now());
		companyMap.put("CreateDate", date);
		
		int ret = 0;
		String insertStr = "INSERT into Companies (" ;
		
		String insertStr_values = "(" ;
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> companyDetail : companyMap.entrySet()) {

				insertStr += companyDetail.getKey() + ", ";

				if (companyDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (companyDetail.getValue()) + ", ";
				} else if(companyDetail.getValue() instanceof Date ) {
					insertStr_values += "'" + (Date) (companyDetail.getValue()) + "' , ";
				}
				else
				{
					insertStr_values += "'" + (String) (companyDetail.getValue()) + "' , ";
				}
				

			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			System.out.println(completInsertStr);

			Query query = session.createNativeQuery(completInsertStr);
			
			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("Companymap " + companyMap);
			

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return ret;
	}

	public static Integer InsertCampaignDetails( HashMap<String, Object> campaignMap) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();
		Date date= Date.valueOf(LocalDate.now());
		campaignMap.put("CreateDate", date);
		
		int ret = 0;
		String insertStr = "INSERT into Campaign (" ;
		
		String insertStr_values = "(" ;
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> campaignDetail : campaignMap.entrySet()) {

				insertStr += campaignDetail.getKey() + ", ";

				if (campaignDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (campaignDetail.getValue()) + ", ";
				} else if(campaignDetail.getValue() instanceof Date ) {
					insertStr_values += "'" + (Date) (campaignDetail.getValue()) + "' , ";
				}
				else
			{
					insertStr_values += "'" + (String) (campaignDetail.getValue()) + "' , ";
				}
				

			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			System.out.println(completInsertStr);

			Query query = session.createNativeQuery(completInsertStr);
			
			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("Campaignmap " + campaignMap);
			

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return ret;
	}

	public static Integer InsertAdDetails( HashMap<String, Object> AdMap ,CommonsMultipartFile image, CommonsMultipartFile mobile_image) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();
		int ret = 0;
		String insertStr = "INSERT into CampaignAds (";
		
		String insertStr_values = "(";
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> AdDetail : AdMap.entrySet()) {

				insertStr += AdDetail.getKey() + ", ";

				if (AdDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (AdDetail.getValue()) + ", ";
				} else if(AdDetail.getValue() instanceof Date ) {
					insertStr_values += "'" + (Date) (AdDetail.getValue()) + "' , ";
				}
				else
				{
					insertStr_values += "'" + (String) (AdDetail.getValue()) + "' , ";
				}
				

			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			System.out.println(completInsertStr);

			Query query = session.createNativeQuery(completInsertStr);
			
			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("Admap " + AdMap);
			ret=uploadFile(image,mobile_image);
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return ret;
	}

	public static int uploadFile( CommonsMultipartFile image, CommonsMultipartFile mobile_image) {
		
		Session session = HibernateUtil.buildSessionFactory();
		Query query = session.createNativeQuery(
				"SELECT max(ADId) FROM CampaignAds;");
		int res=0;
		try {
			res = (int) query.getSingleResult();
			System.out.println(res);
		} catch (NoResultException e) {
			System.out.println("No Entry");

		}
		String path = System.getProperty( "catalina.base" ) + "/webapps"+ "/cures_articleimages/"+ "cures_adsimages";
		System.out.println(path);
		// path = path+"/uitest";
		String filename = image.getOriginalFilename();
		
		// Rename the file to the new file name
        String renamedFilename = "Ad_" + res + "." + FilenameUtils.getExtension(filename);

		System.out.println(path + "/" + renamedFilename);
		String finalPath="/cures_articleimages/"+ "cures_adsimages" + "/" + renamedFilename;
		try {
			byte barr[] = image.getBytes();

			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + renamedFilename));
			bout.write(barr);
			bout.flush();
			bout.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		if(mobile_image!=null)
		{
			String mobile_path = System.getProperty( "catalina.base" ) + "/webapps"+ "/cures_articleimages/"+ "cures_adsimages/mobile";
			String mobile_filename = mobile_image.getOriginalFilename();
			String mobile_Filename = "Ad_" + res + "." + FilenameUtils.getExtension(mobile_filename);
			try {
				byte barr[] = mobile_image.getBytes();
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(mobile_path + "/" + mobile_Filename));
				bout.write(barr);
				bout.flush();
				bout.close();
			} catch (Exception e) {
				System.out.println(e);
			} 
		}
		Query query1 = session.createNativeQuery(
				  "Update CampaignAds set ImageLocation='" + finalPath + "' where AdID=" + res);
		int ret = 0;
		Transaction transaction= null;
		try {
		    transaction = session.beginTransaction();

		    ret = query1.executeUpdate();

		    transaction.commit();
		} catch (Exception e) {
		    if (transaction != null && transaction.isActive()) {
		        transaction.rollback();
		    }
		    e.printStackTrace();
		} 
		
		return ret;
	}
		
		
	public static Integer InsertAdStats( HashMap<String, Object> StatsMap) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();
		Date date= Date.valueOf(LocalDate.now());
		StatsMap.put("CreateDate", date);
		
		int ret = 0;
		String insertStr = "INSERT into AdsStats (" ;
		
		String insertStr_values = "(" ;
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> statsDetail : StatsMap.entrySet()) {

				insertStr += statsDetail.getKey() + ", ";

				if (statsDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (statsDetail.getValue()) + ", ";
				} else if(statsDetail.getValue() instanceof Date ) {
					insertStr_values += "'" + (Date) (statsDetail.getValue()) + "' , ";
				}
				else
				{
					insertStr_values += "'" + (String) (statsDetail.getValue()) + "' , ";
				}
				

			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			System.out.println(completInsertStr);

			Query query = session.createNativeQuery(completInsertStr);
			
			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("Statsmap " + StatsMap);
			

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return ret;
	}
	public static List CompaniesDetails() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select * from Companies\r\n" + "\r\n" + "");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			Integer CompanyID = (Integer) objects[0];

			String CompanyName = (String) objects[1];

			String CompanyWebsite = (String) objects[2];

			String ContactPerson = (String) objects[3];

			String Email = (String) objects[4];
			String Phone = (String) objects[5];
			Date CreateDate= (Date) objects[6];
			Date LastUpdatedDate= (Date) objects[7];
			Integer Status = (Integer) objects[8];
			hm.put("CompanyID", CompanyID);
			hm.put("CompanyName", CompanyName);
			hm.put("CompanyWebsite", CompanyWebsite);
			hm.put("ContactPerson", ContactPerson);
			hm.put("Email", Email);
			hm.put("Phone", Phone);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("Status", Status);

			hmFinal.add(hm);

		}

		return hmFinal;

	}
	public static List CompaniesDetailsByID(int CompanyID) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select *  from Companies where CompanyID =" + CompanyID + ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			String CompanyName = (String) objects[1];

			String CompanyWebsite = (String) objects[2];

			String ContactPerson = (String) objects[3];

			String Email = (String) objects[4];
			String Phone = (String) objects[5];
			Date CreateDate= (Date) objects[6];
			Date LastUpdatedDate= (Date) objects[7];
			Integer Status = (Integer) objects[8];
			
			
			hm.put("CompanyName", CompanyName);
			hm.put("CompanyWebsite", CompanyWebsite);
			hm.put("ContactPerson", ContactPerson);
			hm.put("Email", Email);
			hm.put("Phone", Phone);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("Status", Status);
			hmFinal.add(hm);

		}

		return hmFinal;

	}
	public static List CampaignsDetails() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select * from Campaign\r\n" + "\r\n" + "");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			Integer CampaignID= (Integer) objects[0];

			Integer CompanyID = (Integer) objects[1];

			String CampaignName = (String) objects[2];

			Date StartDate = (Date) objects[3];

			Date EndDate = (Date) objects[4];
			
			Date CreateDate= (Date) objects[5];
			Date LastUpdatedDate= (Date) objects[6];
			Integer Status = (Integer) objects[7];
			hm.put("CampaignID", CampaignID);
			hm.put("CompanyID", CompanyID);
			hm.put("CampaignName", CampaignName);
			hm.put("StartDate", StartDate);
			hm.put("EndDate", EndDate);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("Status", Status);

			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List CampaignsDetailsByID(int CampaignID) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select *  from Campaign where CampaignID =" + CampaignID + ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			
			Integer CompanyID = (Integer) objects[1];

			String CampaignName = (String) objects[2];

			Date StartDate = (Date) objects[3];

			Date EndDate = (Date) objects[4];
			
			Date CreateDate= (Date) objects[5];
			Date LastUpdatedDate= (Date) objects[6];
			Integer Status = (Integer) objects[7];
			hm.put("CompanyID", CompanyID);
			hm.put("CampaignName", CampaignName);
			hm.put("StartDate", StartDate);
			hm.put("EndDate", EndDate);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("Status", Status);
			hmFinal.add(hm);

		}
		return hmFinal;
	}
	
	public static List CampaignsAds() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				 "SELECT ca.AdID, ca.CampaignID, cam.CampaignName, ca.DiseaseCondition, dc.dc_desc, ca.SlotID, at.SlotName,\r\n"
				 + "       ca.AdTitle, ca.AdDescription, ca.AdCount, ca.AdDelivered, ca.ImageLocation, ca.ImageAltText,\r\n"
				 + "       ca.StartDate, ca.EndDate, ca.CreateDate, ca.LastUpdatedDate, ca.ReviewStatus, ca.PaymentStatus,ca.AdTypeID,target.AdTypeName\r\n"
				 + "FROM CampaignAds ca\r\n"
				 + "LEFT JOIN disease_condition dc ON ca.DiseaseCondition = dc.dc_id\r\n"
				 + "LEFT JOIN Campaign cam ON cam.CampaignID = ca.CampaignID\r\n"
				 + "LEFT JOIN AdsSlotTypes at ON ca.SlotID = at.SlotID\r\n"
				 + "LEFT JOIN AdsTypes target ON ca.AdTypeID = target.AdTypeID;\r\n"
				 + "");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			Integer AdID= (Integer) objects[0];
			Integer CampaignID=(Integer) objects[1];
			String CampaignName = (String) objects[2];
			Integer DiseaseConditionID=(Integer) objects[3];
			
			String DiseaseConditionName = (String) objects[4];

			Integer SlotID = (Integer) objects[5];
			String SlotName=(String)objects[6];
			String AdTitle = (String) objects[7];
			
			String AdDescription= (String) objects[8];

			Integer AdCount = (Integer) objects[9];

			Integer AdDelivered = (Integer) objects[10];

			String ImageLocation = (String) objects[11];

			String ImageAltText= (String) objects[12];
			Date StartDate = (Date) objects[13];

			Date EndDate = (Date) objects[14];
				
			Date CreateDate= (Date) objects[15];
			Date LastUpdatedDate= (Date) objects[16];
			
			Integer ReviewStatus= (Integer) objects[17];

			Integer PaymentStatus= (Integer) objects[18];
			Integer AdTypeID= (Integer) objects[19];
			String AdTypeName= (String) objects[20];
			hm.put("AdID", AdID);
			hm.put("CampaignID", CampaignID);
			hm.put("CampaignName", CampaignName);
			hm.put("DiseaseConditionID", DiseaseConditionID);
			hm.put("DiseaseConditionName", DiseaseConditionName);
			hm.put("SlotID", SlotID);
			hm.put("SlotName", SlotName);
			hm.put("AdTitle", AdTitle);
			hm.put("AdDescription", AdDescription);
			hm.put("AdCount", AdCount);
			hm.put("AdDelivered", AdDelivered);
			hm.put("ImageLocation", ImageLocation);
			hm.put("ImageAltText", ImageAltText);
			hm.put("StartDate", StartDate);
			hm.put("EndDate", EndDate);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("ReviewStatus", ReviewStatus);
			hm.put("PaymentStatus", PaymentStatus);
			hm.put("AdTypeID", AdTypeID);
			hm.put("AdTypeName", AdTypeName);
			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List CampaignsAdsByID(int AdID) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"SELECT ca.CampaignID, cam.CampaignName, ca.DiseaseCondition, dc.dc_desc, ca.SlotID, at.SlotName,\r\n"
				+ "       ca.AdTitle, ca.AdDescription, ca.AdCount, ca.AdDelivered, ca.ImageLocation, ca.ImageAltText,\r\n"
				+ "       ca.StartDate, ca.EndDate, ca.CreateDate, ca.LastUpdatedDate, ca.ReviewStatus, ca.PaymentStatus,ca.AdTypeID,target.AdTypeName\r\n"
				+ "FROM CampaignAds ca\r\n"
				+ "LEFT JOIN disease_condition dc ON ca.DiseaseCondition = dc.dc_id\r\n"
				+ "LEFT JOIN Campaign cam ON cam.CampaignID = ca.CampaignID\r\n"
				+ "LEFT JOIN AdsSlotTypes at ON ca.SlotID = at.SlotID\r\n"
				 + "LEFT JOIN AdsTypes target ON ca.AdTypeID = target.AdTypeID\r\n"
				+ "where AdID =" + AdID + ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			
			Integer CampaignID=(Integer) objects[0];
			String CampaignName = (String) objects[1];
			Integer DiseaseConditionID=(Integer) objects[2];
			
			String DiseaseConditionName = (String) objects[3];

			Integer SlotID = (Integer) objects[4];
			String SlotName=(String)objects[5];
			String AdTitle = (String) objects[6];
			
			String AdDescription= (String) objects[7];

			Integer AdCount = (Integer) objects[8];

			Integer AdDelivered = (Integer) objects[9];

			String ImageLocation = (String) objects[10];

			String ImageAltText= (String) objects[11];
			Date StartDate = (Date) objects[12];

			Date EndDate = (Date) objects[13];
				
			Date CreateDate= (Date) objects[14];
			Date LastUpdatedDate= (Date) objects[15];
			
			Integer ReviewStatus= (Integer) objects[16];

			Integer PaymentStatus= (Integer) objects[17];
			Integer AdTypeID= (Integer) objects[18];
			String AdTypeName= (String) objects[19];
			hm.put("CampaignID", CampaignID);
			hm.put("CampaignName", CampaignName);
			hm.put("DiseaseConditionID", DiseaseConditionID);
			hm.put("DiseaseConditionName", DiseaseConditionName);
			hm.put("SlotID", SlotID);
			hm.put("SlotName", SlotName);
			hm.put("AdTitle", AdTitle);
			hm.put("AdDescription", AdDescription);
			hm.put("AdCount", AdCount);
			hm.put("AdDelivered", AdDelivered);
			hm.put("ImageLocation", ImageLocation);
			hm.put("ImageAltText", ImageAltText);
			hm.put("StartDate", StartDate);
			hm.put("EndDate", EndDate);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hm.put("ReviewStatus", ReviewStatus);
			hm.put("PaymentStatus", PaymentStatus);
			hm.put("AdTypeID", AdTypeID);
			hm.put("AdTypeName", AdTypeName);

			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List AdsStatsByID(int StatsID) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select *  from AdsStats where StatsID =" + StatsID + ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			

			Integer AdID = (Integer) objects[0];

			Integer SlotTypeID = (Integer) objects[1];

			Integer Impressions = (Integer) objects[2];

			Integer Clicks = (Integer) objects[3];

			Date CreateDate= (Date) objects[4];
			Date LastUpdatedDate= (Date) objects[5];
			
			Integer AdTargetID= (Integer) objects[6];
			
			hm.put("AdID", AdID);
			hm.put("SlotTypeID", SlotTypeID);
			hm.put("Impressions", Impressions);
			hm.put("Clicks", Clicks);
			hm.put("AdTargetID", AdTargetID);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			hmFinal.add(hm);

		}

		return hmFinal;

	}
	public static int updateCompanyId(int CompanyID, HashMap companyMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		String updatestr = "";
		if (companyMap.containsKey("CompanyName")) {
			updatestr += "`CompanyName` = '" + companyMap.get("CompanyName") + "',\r\n";
		
		}
		
		if (companyMap.containsKey("CompanyWebsite")) {
			updatestr += "`CompanyWebsite` = '" + companyMap.get("CompanyWebsite") + "',\r\n";
		}
		if (companyMap.containsKey("ContactPerson")) {
			updatestr += "`ContactPerson` = '" + companyMap.get("ContactPerson") + "',\r\n";
		}
		if (companyMap.containsKey("Email")) {
			updatestr += "`Email` = '" + companyMap.get("Email") + "',\r\n";
		}
		if (companyMap.containsKey("Phone")) {
			updatestr += "`Phone` = '" + companyMap.get("Phone") + "',\r\n";
		}
		
		if (companyMap.containsKey("Status")) {
			updatestr += "`Status` = '" + companyMap.get("Status") + "',\r\n";
		}
		updatestr+="LastUpdateddate='"  + date +  "'  ";
		System.out.println();
		Query query = session.createNativeQuery(
				"UPDATE `Companies`\r\n" + "SET\r\n" + updatestr + "WHERE `CompanyID` = " + CompanyID + ";");
		int ret = 0;
		
		try {
		ret = query.executeUpdate();
		session.getTransaction().commit();
		System.out.println("updated  table for CompanyID =  " + CompanyID);
	
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ret;
	}
	
	public static int updateCampaignId(int CampaignID, HashMap campaignMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		String updatestr = "";
		if (campaignMap.containsKey("CompanyID")) {
			updatestr += "`CompanyID` = '" + campaignMap.get("CompanyID") + "',\r\n";
		
		}
		
		if (campaignMap.containsKey("CampaignName")) {
			updatestr += "`CampaignName` = '" + campaignMap.get("CampaignName") + "',\r\n";
		}
		if (campaignMap.containsKey("StartDate")) {
			updatestr += "`StartDate` = '" + campaignMap.get("StartDate") + "',\r\n";
		}
		if (campaignMap.containsKey("EndDate")) {
			updatestr += "`EndDate` = '" + campaignMap.get("EndDate") + "',\r\n";
		}
		
		if (campaignMap.containsKey("Status")) {
			updatestr += "`Status` = '" + campaignMap.get("Status") + "',\r\n";
		}
		updatestr+="LastUpdateddate='"  + date +  "'  ";
		System.out.println();
		Query query = session.createNativeQuery(
				"UPDATE `Campaign`\r\n" + "SET\r\n" + updatestr + "WHERE `CampaignID` = " + CampaignID + ";");
		int ret = 0;
		try {
		ret = query.executeUpdate();
		session.getTransaction().commit();
		System.out.println("updated  table for CampaignID =  " + CampaignID);
	
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ret;
	}
	
	public static int updateCampaignAdsId(int AdID, HashMap campaignAdsMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		String updatestr = "";
		if (campaignAdsMap.containsKey("CampaignID")) {
			updatestr += "`CampaignID` = '" + campaignAdsMap.get("CampaignID") + "',\r\n";
		
		}
		
		if (campaignAdsMap.containsKey("DiseaseCondition")) {
			updatestr += "`DiseaseCondition` = '" + campaignAdsMap.get("DiseaseCondition") + "',\r\n";
		}
		if (campaignAdsMap.containsKey("SlotID")) {
			updatestr += "`SlotID` = '" + campaignAdsMap.get("SlotID") + "',\r\n";
		}
		if (campaignAdsMap.containsKey("AdTitle")) {
			updatestr += "`AdTitle` = '" + campaignAdsMap.get("AdTitle") + "',\r\n";
		}
		
		
		
		if (campaignAdsMap.containsKey("AdDescription")) {
			updatestr += "`AdDescription` = '" + campaignAdsMap.get("AdDescription") + "',\r\n";
		
		}
		
		if (campaignAdsMap.containsKey("AdCount")) {
			updatestr += "`AdCount` = '" + campaignAdsMap.get("AdCount") + "',\r\n";
		}
		
		if (campaignAdsMap.containsKey("AdDelivered")) {
			updatestr += "`AdDelivered` = '" + campaignAdsMap.get("AdDelivered") + "',\r\n";
		
		}
		
		if (campaignAdsMap.containsKey("ImageLocation")) {
			updatestr += "`ImageLocation` = '" + campaignAdsMap.get("ImageLocation") + "',\r\n";
		}
		

		if (campaignAdsMap.containsKey("ImageAltText")) {
			updatestr += "`ImageAltText` = '" + campaignAdsMap.get("ImageAltText") + "',\r\n";
		}
		
		
		if (campaignAdsMap.containsKey("StartDate")) {
			updatestr += "`StartDate` = '" + campaignAdsMap.get("StartDate") + "',\r\n";
		}
		if (campaignAdsMap.containsKey("EndDate")) {
			updatestr += "`EndDate` = '" + campaignAdsMap.get("EndDate") + "',\r\n";
		}
		
		if (campaignAdsMap.containsKey("ReviewStatus")) {
			updatestr += "`ReviewStatus` = '" + campaignAdsMap.get("ReviewStatus") + "',\r\n";
		}
		if (campaignAdsMap.containsKey("PaymentStatus")) {
			updatestr += "`PaymentStatus` = '" + campaignAdsMap.get("PaymentStatus") + "',\r\n";
		}
		if (campaignAdsMap.containsKey("AdTypeID")) {
			updatestr += "`AdTypeID` = '" + campaignAdsMap.get("AdTypeID") + "',\r\n";
		}
		
		
		updatestr+="LastUpdateddate='"  + date +  "'  ";
		System.out.println();
		Query query = session.createNativeQuery(
				"UPDATE `CampaignAds`\r\n" + "SET\r\n" + updatestr + "WHERE `AdID` = " + AdID + ";");
		int ret = 0;
		try {
		ret = query.executeUpdate();
		session.getTransaction().commit();
		System.out.println("updated  table for AdID =  " + AdID);
	
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ret;
	}

	public static int updateAdsStatsId(int StatsID, HashMap StatsMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		String updatestr = "";
		if (StatsMap.containsKey("AdID")) {
			updatestr += "`AdID` = '" + StatsMap.get("AdID") + "',\r\n";
		
		}
		
		if (StatsMap.containsKey("SlotTypeID")) {
			updatestr += "`SlotTypeID` = '" + StatsMap.get("SlotTypeID") + "',\r\n";
		}
		if (StatsMap.containsKey("Impressions")) {
			updatestr += "`Impressions` = '" + StatsMap.get("Impressions") + "',\r\n";
		}
		if (StatsMap.containsKey("Clicks")) {
			updatestr += "`Clicks` = '" + StatsMap.get("Clicks") + "',\r\n";
		}
		
		if (StatsMap.containsKey("AdTargetID")) {
			updatestr += "`AdTargetID` = '" + StatsMap.get("AdTargetID") + "',\r\n";
		}
		updatestr+="LastUpdateddate='"  + date +  "'  ";
		System.out.println();
		Query query = session.createNativeQuery(
				"UPDATE `AdsStats`\r\n" + "SET\r\n" + updatestr + "WHERE `StatsID` = " + StatsID + ";");
		int ret = 0;
		try {
		ret = query.executeUpdate();
		session.getTransaction().commit();
		System.out.println("updated  table for StatsID =  " + StatsID);
	
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ret;
	}
	public static int deleteCampaignId(int CampaignID) {
		
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		 String updatestr = " status = 0 , ";
		 updatestr+="LastUpdateddate='"  + date +  "'  ";
		 System.out.println(updatestr);
			Query query = session.createNativeQuery(
					"UPDATE `Campaign`\r\n" + "SET\r\n" + updatestr + "WHERE `CampaignID` = " + CampaignID + ";");
			int ret = 0;
			try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("deleted entry for CampaignID =  " + CampaignID);
		
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		 
		 
	}
	
public static int deleteCompanyId(int CompanyID) {
		
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 Date date= Date.valueOf(LocalDate.now());
		 String updatestr = " status = 0 , ";
		 updatestr+="LastUpdateddate='"  + date +  "'  ";
		 System.out.println(updatestr);
			Query query = session.createNativeQuery(
					"UPDATE `Companies`\r\n" + "SET\r\n" + updatestr + "WHERE `CompanyID` = " + CompanyID + ";");
			int ret = 0;
			try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("deleted entry for CompanyID =  " + CompanyID);
		
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		 
		 
	}

	public static List ListCompanies() {

	Session session = HibernateUtil.buildSessionFactory();

	Query query = session.createNativeQuery(
			"Select CompanyID, CompanyName from Companies where Status = 1 ;");
	List<Object[]> results = (List<Object[]>) query.getResultList();
	System.out.println(results.size());

	List<Object[]> arrayDataList = new ArrayList<>();
	for (Object[] objects : results) {
	    Integer CompanyID = (Integer) objects[0];
	    String CompanyName = (String) objects[1];

	    Object[] dataArray = new Object[]{CompanyID, CompanyName};
	    arrayDataList.add(dataArray);

	}

	return arrayDataList;

	}

public static List ListCampaigns() {

	Session session = HibernateUtil.buildSessionFactory();

	Query query = session.createNativeQuery(
			"Select CampaignID, CampaignName from Campaign where Status = 1 ;");
	List<Object[]> results = (List<Object[]>) query.getResultList();
	System.out.println(results.size());

	List<Object[]> arrayDataList = new ArrayList<>();
	for (Object[] objects : results) {
	    Integer CampaignID = (Integer) objects[0];
	    String CampaignName = (String) objects[1];

	    Object[] dataArray = new Object[]{CampaignID, CampaignName};
	    arrayDataList.add(dataArray);

	}

	return arrayDataList;

	}
	public static List ListAdsTypes() {

	Session session = HibernateUtil.buildSessionFactory();

	Query query = session.createNativeQuery(
			"Select AdTypeID, AdTypeName from AdsTypes ;");
	List<Object[]> results = (List<Object[]>) query.getResultList();
	System.out.println(results.size());

	List<Object[]> arrayDataList = new ArrayList<>();
	for (Object[] objects : results) {
	    Integer AdTypeID = (Integer) objects[0];
	    String AdTypeName = (String) objects[1];

	    Object[] dataArray = new Object[]{AdTypeID, AdTypeName};
	    arrayDataList.add(dataArray);

	}

	return arrayDataList;

	}
	
	
	public static List ListAdsTargetTypes() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select AdTargetID, AdTargetName from AdsTargetTypes;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());

		List<Object[]> arrayDataList = new ArrayList<>();
		for (Object[] objects : results) {
		    Integer AdTargetID = (Integer) objects[0];
		    String AdTargetName = (String) objects[1];

		    Object[] dataArray = new Object[]{AdTargetID, AdTargetName};
		    arrayDataList.add(dataArray);

		}

		return arrayDataList;

		}
		
	public static List ListAdsSlotTypes() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select SlotID,SlotName from AdsSlotTypes where Status = 1 ;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());

		List<Object[]> arrayDataList = new ArrayList<>();
		for (Object[] objects : results) {
		    Integer SlotID = (Integer) objects[0];
		    String SlotName = (String) objects[1];

		    Object[] dataArray = new Object[]{SlotID, SlotName};
		    arrayDataList.add(dataArray);

		}

		return arrayDataList;

		}
	public static String AdsURL(Integer AdType, Integer DC_Cond) throws JsonProcessingException {
    String URL = null;
    System.out.println("AdType" + AdType);
    Integer total = 0;
    Integer displayed = 0;
    boolean flag = false;
    LocalDate currentDate = LocalDate.now();
    String key = null;

    if (mcc == null) {
        initializeCacheClient();
    }
    boolean val = mcc.getAvailableServers().isEmpty();
    System.out.println("Memcached status:" + val);
    if (val) {
        System.out.println("Memcached Restarted");
        DailyTaskScheduler.performDailyCalculationsAndCacheUpdate();
        DailyTaskScheduler.DisplayPattern();
        TargetAds.update();
        flag = true;
    }
    try {
        if (!val || flag) {
            if (DC_Cond != 0) {
                String count1 = TargetAds.DC_AdCount.get(DC_Cond.toString());
                System.out.println("count1" + count1);
                if (count1 != null) {
                    String[] parts = count1.split(":");
                    total = Integer.parseInt(parts[0]);
                    displayed = Integer.parseInt(parts[1]);
                    System.out.println(total + displayed);
                    if (displayed < total) {
                        String key1 = "Left_" + DC_Cond + "_" + displayed;
                        URL = (String) mcc.get(key1);
                    } else {
                        System.out.println("DC_Conds are served");
                        AdType = 2;
                    }
                } else {
                    AdType = 2;
                }
            }
            if (AdType == 1) {
                int count = BannerCountMap.getOrDefault(currentDate, 0);
                key = "Banner_0_" + String.valueOf(count);
                System.out.println(key);
                URL = (String) mcc.get(key);
                System.out.println(key + URL);
                if (lastRequestDate == null || !lastRequestDate.equals(currentDate)) {
                    BannerCountMap.clear();
                    lastRequestDate = currentDate;
                }
            }
            if (AdType == 2) {
                int count = LeftCountMap.getOrDefault(currentDate, 0);
                key = "Left_0_" + String.valueOf(count);
                URL = (String) mcc.get(key);
                if (lastRequestDate == null || !lastRequestDate.equals(currentDate)) {
                    LeftCountMap.clear();
                    lastRequestDate = currentDate;
                }
            }
            if (URL == null) {
                URL = "All Ads are Served";
            } else {
                if (AdType == 1) {
                    BannerCountMap.put(currentDate, BannerCountMap.getOrDefault(currentDate, 0) + 1);
                } else if (AdType == 2) {
                    LeftCountMap.put(currentDate, LeftCountMap.getOrDefault(currentDate, 0) + 1);
                } else {
                    Integer remaining = displayed + 1;
                    TargetAds.DC_AdCount.put(DC_Cond.toString(), total + ":" + remaining);
                    System.out.println("TargetAds.DC_AdCount" + TargetAds.DC_AdCount);
                }
                try {
                    // update(URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } finally {
        if (mcc != null) {
            mcc.shutdown(); // Release the MemcachedClient resources
        }
    }
    System.out.println("Current Date in Milliseconds: before sending the response " + System.currentTimeMillis());
    return URL;
}

	  @Async
	public static void update(String URL)
    {
    	Session session1 = HibernateUtil.buildSessionFactory();
    	session1.beginTransaction();
	System.out.println("Current Date in Milliseconds: before update " + System.currentTimeMillis());
    	Query query = session1.createNativeQuery(
   			"Update CampaignAds set AdDelivered=AdDelivered + 1 WHERE ImageLocation = '" + URL + "';");
    	try {
			int ret = query.executeUpdate();
			System.out.println(ret);
			session1.getTransaction().commit();
	System.out.println("Current Date in Milliseconds: after update " + System.currentTimeMillis());
//		store(URL);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@Async
	  public static void store(String URL)
	    {
	    	Session session = HibernateUtil.buildSessionFactory();
	    	session.beginTransaction();
	    	int res=0;
	    	Integer AdID = null;
	    	Integer CampaignAdID=null;
	    	Query query1 = session.createNativeQuery(
					"Select stats.AdID ID1, c.AdID  ID2 from AdsStats stats inner join CampaignAds  c where  stats.AdID=c.AdID and c.ImageLocation= '" + URL + "';");
	    	List<Object[]> results = (List<Object[]>) query1.getResultList();
			
			for (Object[] objects : results) {
			
				AdID = (Integer) objects[0];
				System.out.println("AdID" + AdID);
				
			}
			
			if (AdID == null)
			{
				System.out.println("Current Date in Milliseconds: before insert " + System.currentTimeMillis());
				Query query = session.createNativeQuery(
		    			"INSERT INTO AdsStats (AdID, Impressions) SELECT c.AdID, 1 FROM CampaignAds c WHERE c.ImageLocation = '" + URL + "';");
		    		System.out.println(query);
		    	try {
					int ret = query.executeUpdate();
					session.getTransaction().commit();
		System.out.println("Current Date in Milliseconds: after insert " + System.currentTimeMillis());

					System.out.println(" entry for URL =  " + URL);
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
		System.out.println("Current Date in Milliseconds: before update " + System.currentTimeMillis());
					Query query = session.createNativeQuery(
		    			"UPDATE AdsStats\r\n"
		    			+ "JOIN CampaignAds ON AdsStats.AdID = CampaignAds.AdID\r\n"
		    			+ "SET AdsStats.Impressions = AdsStats.Impressions + 1\r\n"
		    			+ "WHERE CampaignAds.ImageLocation = '" + URL + "';");
		    		
		    	try {
					int ret = query.executeUpdate();
					System.out.println(ret);
					session.getTransaction().commit();
		System.out.println("Current Date in Milliseconds: after update adsstats " + System.currentTimeMillis());

					System.out.println(" entry for URL =  " + URL);
					
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
	    }

	public static HashMap getParentDiseaseId(int article_id) {
		Session session = HibernateUtil.buildSessionFactory();
		Query query = session.createNativeQuery(
				"SELECT article_id, dc_id, parent_dc_id FROM article a join disease_condition d\n"
				+ "where a.disease_condition_id = d.dc_id && article_id="+article_id+"\n"
				+ ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results);
		System.out.println(results.get(0));
		Integer parent_disease_id=null;
		Integer dc_id=null;
		HashMap<String, Integer> hm_dc_id= new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> hm_parent_disease_id= new LinkedHashMap<String, Integer>();
		for (Object[] res: results) {
			
			dc_id=(Integer) res[1];
			parent_disease_id = (Integer) res[2];
			hm_dc_id.put("parent_dc_id", dc_id);
			hm_parent_disease_id.put("parent_dc_id", parent_disease_id);
		}
		System.out.println(dc_id);
		System.out.println(parent_disease_id);
//		System.out.println(hm);
		if(parent_disease_id== null) {
			return hm_dc_id;
		}else {
			return hm_parent_disease_id;
		}
		
	}

	public static int clicksIncrement(int adId) {
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();
		 
			Query query = session.createNativeQuery("UPDATE AdsStats\n"
					+ "SET clicks = clicks+ 1\n"
					+ "where AdID =" + adId);
			int ret = 0;
			try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updating clicks in adstats =  " + adId);
			 
		
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}

	public static List getnameParentDisease() {
		 

			Session session = HibernateUtil.buildSessionFactory();

			Query query = session.createNativeQuery(
					"select parent_dc_id,dc_desc, dc_id from disease_condition;");
			List<Object[]> results = (List<Object[]>) query.getResultList();
			System.out.println(results.size());
              
			List<Object[]> arrayDataList = new ArrayList<>();
			for (Object[] objects : results) {
				if(null == (Integer) objects[0]) {
					Integer disease_ID = (Integer) objects[2];
					
					String AdTypeName = (String) objects[1];
               
				    Object[] dataArray = new Object[]{disease_ID, AdTypeName};
				    arrayDataList.add(dataArray);
				}
			}

			return arrayDataList;
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

	
	public static List<LinkedHashMap<String,Object>>  searchCompanies_byCompanyName(HashMap companies)  {

		Session session = HibernateUtil.buildSessionFactory();
        
        Query query=null;
        
         try {
        	 
            
        
        if(companies.containsKey("CompanyName")) { 
        	String CompanyName = (String) companies.get("CompanyName");
        	query = session.createNativeQuery(
    				"SELECT * "
    				+ " FROM Companies where CompanyName= '"+CompanyName + "';");
        }else if(companies.containsKey("CreateDate")) {
        	java.sql.Date sqlDate= string_to_Date((String)companies.get("CreateDate"));
        	query = session.createNativeQuery(
                    "SELECT * FROM Companies WHERE CreateDate= :createDate"
                );
                query.setParameter("createDate", sqlDate);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
		List<LinkedHashMap<String,Object>> list= new ArrayList<LinkedHashMap<String,Object>>();
		try {
			List<Object[]> results = (List<Object[]>) query.getResultList();
			System.out.println(results);
			for (Object[] res: results) {
				LinkedHashMap<String,Object> hm= new LinkedHashMap<String, Object>();
				hm.put("CompanyID", res[0]);
				hm.put("CompanyName", res[1]);
				hm.put("CompanyWebsite", res[2]);
				hm.put("ContactPerson", res[3]);
				hm.put("Email", res[4]);
				hm.put("Phone", res[5]);
				hm.put("CreateDate", res[6]);
				hm.put("LastUpdateddate", res[7]);
				hm.put("Status", res[8]);
				list.add(hm);
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return list;
	}
	
	public static List<LinkedHashMap<String,Object>>  searchCompanies_bycampaigns(HashMap campaigns)  {

		Session session = HibernateUtil.buildSessionFactory();
        
        Query query=null;
        

        try {
           
            if(campaigns.containsKey("CampaignName")) { 
            	System.out.println(campaigns.containsKey("CampaignName"));
            	String CampaignName = (String) campaigns.get("CampaignName");
            	query = session.createNativeQuery(
        				"SELECT * "
        				+ " FROM Campaign where CampaignName= '"+CampaignName + "';");
            }else if(campaigns.containsKey("CreateDate")) {
            	java.sql.Date sqlDate= string_to_Date((String)campaigns.get("CreateDate"));
            	query = session.createNativeQuery(
                        "SELECT * FROM Campaign WHERE CreateDate= :createDate"
                    );
                    query.setParameter("createDate", sqlDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        
         
        
        
		List<LinkedHashMap<String,Object>> list= new ArrayList<LinkedHashMap<String,Object>>();
		try {
			List<Object[]> results = (List<Object[]>) query.getResultList();
			for (Object[] res: results) {
				LinkedHashMap<String,Object> hm= new LinkedHashMap<String, Object>();
				hm.put("CampaignID", res[0]);
				hm.put("CompanyID", res[1]);
				hm.put("CampaignName", res[2]);
				hm.put("StartDate", res[3]);
				hm.put("EndDate", res[4]);
				hm.put("CreateDate", res[5]);
				hm.put("LastUpdatedDate", res[6]);
				hm.put("Status", res[7]);
				
				list.add(hm);
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return list;
	}
	
	public static List<LinkedHashMap<String,Object>>  searchCompanies_bycampaignsAds(HashMap campaignsAds)  {

		Session session = HibernateUtil.buildSessionFactory();
        
        Query query=null;
        
        String common_str= "SELECT\n"
				+ "    c.CampaignName,\n"
				+ "    at.AdTypeName,\n"
				+ "    ast.SlotName,\n"
				+ "    ca.*\n"
				+ "FROM\n"
				+ "    Campaign c\n"
				+ "JOIN\n"
				+ "    CampaignAds ca ON c.CampaignID = ca.CampaignID\n"
				+ "JOIN\n"
				+ "    AdsTypes at ON ca.AdTypeID = at.AdTypeID\n"
				+ "JOIN\n"
				+ "    AdsSlotTypes ast ON ca.SlotID = ast.SlotID\n";
        
        
         

        try {
        	             
            if(campaignsAds.containsKey("CampaignName")) { 
            	String CampaignName = (String) campaignsAds.get("CampaignName");
            	query = session.createNativeQuery(
            			common_str + "where CampaignName = '" + CampaignName +"' ;");
            	
            }else if(campaignsAds.containsKey("AdTypeName")) {
            	  String AdTypeName = (String) campaignsAds.get("AdTypeName");
            	
            	
            	query = session.createNativeQuery(
            			common_str + "where AdTypeName = '" + AdTypeName +"' ;");
            	
            }else if(campaignsAds.containsKey("SlotName")) {
          	  String SlotName = (String) campaignsAds.get("SlotName");
          	
          	
          	query = session.createNativeQuery(
          			common_str + "where SlotName = '" + SlotName +"' ;");
          	
          }else if(campaignsAds.containsKey("StartDate")) {
        	  java.sql.Date sqlDate= string_to_Date((String)campaignsAds.get("StartDate"));
        	  query = session.createNativeQuery(
        			  common_str + "WHERE ca.StartDate= :StartDate"
                  );
                  query.setParameter("StartDate", sqlDate);
          	
          }else if(campaignsAds.containsKey("EndDate")) {
        	  java.sql.Date sqlDate= string_to_Date((String)campaignsAds.get("EndDate"));
        	  query = session.createNativeQuery(
        			  common_str + "WHERE ca.EndDate= :EndDate"
                  );
                  query.setParameter("EndDate", sqlDate);
          	
          }else if(campaignsAds.containsKey("CreateDate")) {
        	  java.sql.Date sqlDate= string_to_Date((String)campaignsAds.get("CreateDate"));
        	  query = session.createNativeQuery(
        			  common_str + "WHERE ca.CreateDate= :createDate"
                  );
                  query.setParameter("createDate", sqlDate);
                  
          }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        
         
        
        
		List<LinkedHashMap<String,Object>> list= new ArrayList<LinkedHashMap<String,Object>>();
		try {
			List<Object[]> results = (List<Object[]>) query.getResultList();
			for (Object[] res: results) {
				LinkedHashMap<String,Object> hm= new LinkedHashMap<String, Object>();
				hm.put("CampaignName", res[0]);
				hm.put("AdTypeName", res[1]);
				hm.put("SlotName", res[2]);
				hm.put("AdID", res[3]);
				hm.put("CampaignID", res[4]);
				hm.put("DiseaseCondition", res[5]);
				hm.put("SlotID", res[6]);
				hm.put("AdTitle", res[7]);
				hm.put("AdDescription", res[8]);
				hm.put("AdCount", res[9]);
				hm.put("AdDelivered", res[10]);
				hm.put("ImageLocation", res[11]);
				hm.put("ImageAltText", res[12]);
				hm.put("StartDate", res[13]);
				hm.put("EndDate", res[14]);
				hm.put("CreateDate", res[15]);
				hm.put("LastUpdatedDate", res[16]);
				hm.put("ReviewStatus", res[17]);
				hm.put("PaymentStatus", res[18]);
				hm.put("AdTypeID", res[19]);
				
				
				list.add(hm);
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return list;
	}
	
	public static java.sql.Date string_to_Date(String date) throws ParseException {
		String inputDateString = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = dateFormat.parse(inputDateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
	}

	public static Integer InsertServices( HashMap<String, Object> ServiceMap) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  SponsoredServicesMaster service = new SponsoredServicesMaster();
		  
	        try {
	            // Assuming your HashMap has keys matching the property names in Service
	            // Adjust these names based on your actual Service class
	        	Transaction tx = session.beginTransaction();
				
	            service.setServiceName((String) ServiceMap.get("ServiceName"));
	            service.setServiceDesc((String) ServiceMap.get("ServiceDesc"));
	            service.setPaymentReq((Integer) ServiceMap.get("PaymentReq"));
	            service.setContractReq((Integer) ServiceMap.get("ContractReq"));
	            service.setCreatedBy((Integer) ServiceMap.get("CreatedBy"));
	            service.setStatus((Integer) ServiceMap.get("Status"));
		    service.setAvailabilityReq((Integer) ServiceMap.get("AvailabilityReq"));
	            session.save(service);
	            tx.commit();
	            // Return 1 if insertion is successful
	            return 1;
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the exception or handle it appropriately

	            // Return 0 if insertion fails
	            return 0;
	        }
	    }
	  
	  public static List<SponsoredServicesMaster> getAllServices() {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT\r\n"
		    		+ "    s.ServiceID,\r\n"
		    		+ "    s.ServiceName,\r\n"
		    		+ "    s.ServiceDesc,\r\n"
		    		+ "    s.PaymentReq,\r\n"
		    		+ "    s.ContractReq,\r\n"
				+ "    s.AvailabilityReq,\r\n"			     
		    		+ "    s.CreatedBy,\r\n"
		    		+ "    s.CreatedDate,\r\n"
		    		+ "    s.LastUpdatedDate,\r\n"
		    		+ "    s.Status,\r\n"
		    		+ "    s.UpdatedBy,\r\n"
		    		+ "    CONCAT(regCreated.first_name, ' ', regCreated.last_name) AS CreatedByName,\r\n"
		    		+ "    CONCAT(regUpdated.first_name, ' ', regUpdated.last_name) AS UpdatedByName\r\n"
		    		+ "FROM\r\n"
		    		+ "    SponsoredServicesMaster s\r\n"
		    		+ "JOIN\r\n"
		    		+ "    registration regCreated ON s.CreatedBy = regCreated.registration_id\r\n"
		    		+ "LEFT JOIN\r\n"
		    		+ "    registration regUpdated ON s.UpdatedBy = regUpdated.registration_id;\r\n"
		    		+ "");
		    List<SponsoredServicesMaster> servicesList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " services back", 1);

		    for (Object[] obj : resultList) {
		        SponsoredServicesMaster service = new SponsoredServicesMaster();

		        service.setServiceId(obj[0] != null ? (Integer) obj[0] : 0);
		        service.setServiceName((String) obj[1] != null ? (String) obj[1] : "");
		        service.setServiceDesc((String) obj[2] != null ? (String) obj[2] : "");
		        service.setPaymentReq(obj[3] != null ? (Integer) obj[3] : 0);
		        service.setContractReq(obj[4] != null ? (Integer) obj[4] : 0);
		        service.setAvailabilityReq(obj[5] != null ? (Integer) obj[5] : 0);
		        service.setCreatedBy(obj[6] != null ? (Integer) obj[6] : 0);
		        service.setCreatedDate((Timestamp) (obj[7] != null ? obj[7] : null));
		        service.setLastUpdatedDate((Timestamp) (obj[8] != null ? obj[8] : null));
		        service.setStatus(obj[9] != null ? (Integer) obj[9] : 0);
		        service.setUpdatedBy(obj[10] != null ? (Integer) obj[10] : 0);
		        service.setCreated_Name(obj[11] != null ? (String) obj[11] : "");
		    	service.setUpdated_Name(obj[12] != null ? (String) obj[12] : "");

		        servicesList.add(service);
		    }

		    return servicesList;
		}

	  public static int updateService(Integer ServiceID, HashMap ServiceMap) {
		  String updatestr = "";
		  if (ServiceMap.containsKey("ServiceName")) {
			    updatestr += "ServiceName = '" + ServiceMap.get("ServiceName") + "',\r\n";
			}
			if (ServiceMap.containsKey("ServiceDesc")) {
			    updatestr += "ServiceDesc = '" + ServiceMap.get("ServiceDesc") + "',\r\n";
			}
		  if (ServiceMap.containsKey("PaymentReq")) {
				updatestr += " PaymentReq = " + ServiceMap.get("PaymentReq") + ",\r\n";
			}
		  if (ServiceMap.containsKey("ContractReq")) {
				updatestr += " ContractReq = " + ServiceMap.get("ContractReq") + ",\r\n";
			}
		   if (ServiceMap.containsKey("AvailabilityReq")) {
				updatestr += " AvailabilityReq = " + ServiceMap.get("AvailabilityReq") + ",\r\n";
			}
		  if (ServiceMap.containsKey("UpdatedBy")) {
				updatestr += " UpdatedBy = " + ServiceMap.get("UpdatedBy") + ",\r\n";
			}
		  if (ServiceMap.containsKey("Status")) {
				updatestr += " Status = " + ServiceMap.get("Status") + ",\r\n";
			}
		  updatestr = updatestr.replaceAll(",$", "");
		  Session session = HibernateUtil.buildSessionFactory();
		  session.beginTransaction();

			Query query = session
					.createNativeQuery("UPDATE SponsoredServicesMaster " + "SET " + updatestr + " WHERE ServiceID = " + ServiceID + ";");
			int ret = 0;
			try {
				ret = query.executeUpdate();
				
				Constant.log(">>>>>>updated Services table for ServiceID =  " + ServiceID, 1);

				session.getTransaction().commit();

			} catch (Exception ex) {
				session.getTransaction().rollback();
			} finally {
//				session.getTransaction().commit();   //session.close();
			}
			return ret;
	  }

	  public static int deleteService(int ServiceID) {
			
			Session session = HibernateUtil.buildSessionFactory();

			// creating session object
			//Session session = factory;
			// creating transaction object
			session.beginTransaction();
			
			 String updatestr = " status = 0  ";
			 
			 System.out.println(updatestr);
				Query query = session.createNativeQuery(
						"UPDATE `SponsoredServicesMaster`\r\n" + "SET\r\n" + updatestr + "WHERE `ServiceID` = " + ServiceID + ";");
				int ret = 0;
				try {
				ret = query.executeUpdate();
				session.getTransaction().commit();
				System.out.println("deleted entry for ServiceID =  " + ServiceID);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return ret;
			 	 
		}
  
	  public static List<SponsoredServicesMaster> getService(int ServiceID) {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT * FROM SponsoredServicesMaster where ServiceID ="+ ServiceID + ";");
		    List<SponsoredServicesMaster> servicesList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    
		    for (Object[] obj : resultList) {
		        SponsoredServicesMaster service = new SponsoredServicesMaster();

		        service.setServiceId(obj[0] != null ? (Integer) obj[0] : 0);
		        service.setServiceName((String) obj[1] != null ? (String) obj[1] : "");
		        service.setServiceDesc((String) obj[2] != null ? (String) obj[2] : "");
		        service.setPaymentReq(obj[3] != null ? (Integer) obj[3] : 0);
		        service.setContractReq(obj[4] != null ? (Integer) obj[4] : 0);
		       service.setAvailabilityReq(obj[5] != null ? (Integer) obj[5] : 0);
		        service.setCreatedBy(obj[6] != null ? (Integer) obj[6] : 0);
		        service.setCreatedDate((Timestamp) (obj[7] != null ? obj[7] : null));
		        service.setLastUpdatedDate((Timestamp) (obj[8] != null ? obj[8] : null));
		        service.setStatus(obj[9] != null ? (Integer) obj[9] : 0);
		        service.setUpdatedBy(obj[10] != null ? (Integer) obj[10] : 0);
		        servicesList.add(service);
		    }

		    return servicesList;
		}
	 public static List getServicesListDoc() {
			Session session = HibernateUtil.buildSessionFactory();
			Query query = session.createNativeQuery(
					"SELECT\r\n"
					+ "    s.ServiceName,\r\n"
					+ "    r.first_name,\r\n"
					+ "    c.UserID,\r\n"
					+ "    c.ServiceID,\r\n"
					+ "    r.last_name,\r\n"
					+ "    r.DocID\r\n"
					+ "FROM\r\n"
					+ "    ServiceContractDetails c\r\n"
					+ "LEFT JOIN\r\n"
					+ "    SponsoredServicesMaster s ON c.ServiceID = s.ServiceID\r\n"
					+ "LEFT JOIN\r\n"
					+ "    registration r ON c.UserID = r.registration_id\r\n"
					+ "WHERE\r\n"
					+ "    r.registration_type = 1 AND s.AvailabilityReq=1 \r\n"
					+ "    AND NOT EXISTS (\r\n"
					+ "        SELECT 1\r\n"
					+ "        FROM DoctorAvailability d\r\n"
					+ "        WHERE c.UserID = d.DocID\r\n"
					+ "    );\r\n"
					+ "");
			List<Object[]> results = (List<Object[]>) query.getResultList();
			List<Map<String, Object>> hmFinal = new ArrayList<>();
			Map<Integer, Map<String, Object>> resultMap = new HashMap<>();

			for (Object[] objects : results) {
			    Integer serviceID = (Integer) objects[3];
			     Integer DocID=(Integer) objects[5];
			    // Check if the map already contains information for this serviceID
			    Map<String, Object> hm = resultMap.get(serviceID);
			    
			    if (hm == null) {
			        // If not, create a new map
			        hm = new HashMap<>();
			        resultMap.put(serviceID, hm);
			        hm.put("ServiceID", serviceID);
			    }

			    // Add information for each object
			    hm.put("ServiceName", (String) objects[0]);
			    hm.put("UserID", (Integer) objects[2]);
			    hm.put("DocName", hm.getOrDefault("DocName", "") + " " + (String) objects[1] + " " + (String) objects[4]);
			     hm.put("DocID", DocID);
			}

			// Convert the resultMap values to a list
			hmFinal.addAll(resultMap.values());

			System.out.println(hmFinal);
			return hmFinal;
		}
	public static List getServicesListDoctor() {
		  Session session = HibernateUtil.buildSessionFactory();
		  Query query = session.createNativeQuery(
		      "SELECT\r\n"
		      + "    s.ServiceName,\r\n"
		      + "    r.first_name,\r\n"
		      + "    c.UserID,\r\n"
		      + "    c.ServiceID,\r\n"
		     + "    r.last_name,\r\n"
		      + "    r.DocID\r\n"
		      + "FROM\r\n"
		      + "    ServiceContractDetails c\r\n"
		      + "LEFT JOIN\r\n"
		      + "    SponsoredServicesMaster s ON c.ServiceID = s.ServiceID \r\n"
		      + "LEFT JOIN\r\n"
		      + "    registration r ON c.UserID = r.registration_id\r\n"
		      + "WHERE\r\n"
		      + "    r.registration_type = 1 And s.AvailabilityReq=1;\r\n"
		  );
		  List<Object[]> results = (List<Object[]>) query.getResultList();
		  List<Map<String, Object>> hmFinal = new ArrayList<>();
		  Map<Integer, Map<String, Object>> resultMap = new HashMap<>();

		  for (Object[] objects : results) {
			  HashMap hm = new HashMap();
				String ServiceName = (String) objects[0];
				Integer UserID = (Integer) objects[2];
				Integer ServiceID = (Integer) objects[3];
				String DocName = (String) objects[1]+" " +(String) objects[4] ;
			  	Integer DocID=(Integer) objects[5];
				// Date date1=(Date)objects[4];
				hm.put("ServiceName", ServiceName);
				hm.put("ServiceID", ServiceID);
				hm.put("UserID", UserID);
				hm.put("DocName", DocName);
			  	hm.put("DocID", DocID);
				// hm.put("Date", date1);
				hmFinal.add(hm);
		  System.out.println(hmFinal);
		  }
		  return hmFinal;

		}
	public static Integer InsertContract( HashMap<String, Object> ContractMap, CommonsMultipartFile document) { 
		  Session session = HibernateUtil.buildSessionFactory();
		  ServiceContract contract = new ServiceContract();
		   int result=0;
		  String filename="";
		  if(document!=null)
		  {
			  result=uploadContractFile(document);
			  filename=document.getOriginalFilename();
			  System.out.println("Filename"+filename);
		  }
		  
		  if(result==0)
		  {
			 System.out.println("Not uploading the document"); 
		  }
			  try {
		            // Assuming your HashMap has keys matching the property names in Service
		            // Adjust these names based on your actual Service class
		        	Transaction tx = session.beginTransaction();
		        	contract.setServiceId((Integer) ContractMap.get("ServiceID"));
		        	contract.setUserId((Integer) ContractMap.get("UserID"));
		        	contract.setContactFirstName((String) ContractMap.get("ContactFirstName"));
		        	contract.setContactLastName((String) ContractMap.get("ContactLastName"));
		        	contract.setDocumentPath(filename);
		        	contract.setStartDate((String) ContractMap.get("StartDate"));
		        	contract.setEndDate((String) ContractMap.get("EndDate"));
		        	contract.setFee((String) ContractMap.get("Fee"));
		        	contract.setCurrency((String) ContractMap.get("Currency"));
		        	contract.setCreatedBy((Integer) ContractMap.get("CreatedBy"));
		        	contract.setStatus((Integer) ContractMap.get("Status"));
		            session.save(contract);
		            tx.commit();
				System.out.println("Contract added");
		            // Return 1 if insertion is successful
		            return 1;
		        } catch (Exception e) {
		            e.printStackTrace(); // Log the exception or handle it appropriately

		            // Return 0 if insertion fails
		            return 0;
		        }  
		  
	    }
	  
	    public static int uploadContractFile( CommonsMultipartFile image) {
	
	    	String path = System.getProperty( "catalina.base" ) + "/webapps/"+ "cures_articleimages/"+ "contracts";

		String filename = image.getOriginalFilename();
		String finalPath=path + "/" + filename;
	        System.out.println(path + "/" + filename);
			int ret=0;	
			try {
				byte barr[] = image.getBytes();
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
				bout.write(barr);
				bout.flush();
				bout.close();
				ret=1;
			} catch (Exception e) {
				System.out.println(e);
			}
			
	    	return ret;
	    	
	    }
	  public static List<ServiceContract> getAllContracts() {
		    Session session = HibernateUtil.buildSessionFactory();
		    Query query1 = session.createNativeQuery("SELECT\r\n"
		    		+ "    c.ContractID,\r\n"
		    		+ "    c.ServiceID,\r\n"
		    		+ "    s.ServiceName,\r\n"
		    		+ "    r.first_name,\r\n"
		    		+ "    c.UserID,\r\n"
		    		+ "    c.ContactFirstName,\r\n"
		    		+ "    c.ContactLastName,\r\n"
		    		+ "    c.CreatedBy,\r\n"
		    		+ "    c.DocumentPath,\r\n"
		    		+ "    c.StartDate,\r\n"
		    		+ "    c.EndDate,\r\n"
		    		+ "    c.Fee,\r\n"
		    		+ "    c.Currency,\r\n"
		    		+ "    c.CreatedDate,\r\n"
		    		+ "    c.LastUpdatedDate,\r\n"
		    		+ "    c.Status,\r\n"
		    		+ "    c.UpdatedBy,\r\n"
		    		+ "    r.last_name,\r\n"
		    		+"	CONCAT(reg.first_name, ' ', reg.last_name) AS Created_Name,\r\n"
			    	+"      CONCAT(reg1.first_name, ' ', reg1.last_name) AS Updated_Name\r\n"
		    		+ "FROM\r\n"
		    		+ "    ServiceContractDetails c\r\n"
		    		+ "JOIN\r\n"
		    		+ "    SponsoredServicesMaster s ON c.ServiceID = s.ServiceID\r\n"
				+"JOIN\r\n"
		    		+" registration reg ON c.CreatedBy = reg.registration_id\r\n"
		    		+"LEFT JOIN\r\n"
		    	        +" registration reg1 ON c.UpdatedBy = reg1.registration_id\r\n"
		    		+ "LEFT JOIN\r\n"
		    		+ "    registration r ON c.UserID = r.registration_id\r\n"
		    		+ "");
		    List<ServiceContract> contractsList = new ArrayList<>();
		    
		    List<Object[]> resultList = query1.getResultList();
		    Constant.log("Executed Query and Got: " + resultList.size() + " Services Contracts back", 1);

		    for (Object[] obj : resultList) {
		    	ServiceContract contract = new ServiceContract();

		    	contract.setContractId(obj[0] != null ? (Integer) obj[0] : 0);
		    	contract.setServiceId(obj[1] != null ? (Integer) obj[1] : 0);
		    	contract.setServiceName((String) obj[2] != null ? (String) obj[2] : "");
		    	contract.setUserName((String) obj[3] != null? ((String) obj[17] != null? (String) obj[3] + " " + (String) obj[17]: (String) obj[3]) : "");
		    	contract.setUserId(obj[4] != null ? (Integer) obj[4] : 0);
		    	contract.setContactFirstName((String) obj[5] != null ? (String) obj[5] : "");
		    	contract.setContactLastName((String) obj[6] != null ? (String) obj[6] : "");
		    	contract.setCreatedBy(obj[7] != null ? (Integer) obj[7] : 0);
		    	contract.setDocumentPath((String) obj[8] != null ? (String) obj[8] : "");
		    	contract.setStartDate((obj[9] != null ? obj[9].toString() : null));
		    	contract.setEndDate( (obj[10] != null ? obj[10].toString() : null));
		    	contract.setFee( (obj[11] != null ? obj[11].toString() : null));
		    	contract.setCurrency((String) (obj[12] != null ? obj[12] : null));
		    	contract.setCreatedDate((Timestamp) (obj[13] != null ? obj[13] : null));
		    	contract.setLastUpdatedDate((Timestamp) (obj[14] != null ? obj[14] : null));
		    	contract.setStatus(obj[15] != null ? (Integer) obj[15] : 0);
		    	contract.setUpdatedBy(obj[16] != null ? (Integer) obj[16] : 0);
		    	contract.setCreated_Name(obj[18] != null ? (String) obj[18] : "");
		    	contract.setUpdated_Name(obj[19] != null ? (String) obj[19] : "");

		    	contractsList.add(contract);
		    }

		    return contractsList;
		}


	// Method to update a ServiceContract based on ContractID and provided ContractMap
	    
	  public static int updateContract(Integer ContractID, HashMap ContractMap,CommonsMultipartFile document) {
		// String to store the update clauses
	        	String updatestr = "";
			int result=0;
			String filename="";
			if(document!=null)
			  {
				  result=uploadContractFile(document);
				  filename=document.getOriginalFilename();
				  System.out.println("Filename"+filename);
				  updatestr += " DocumentPath = '" +filename +"',\r\n";
			  }
			  
			  if(result==0)
			  {
				 System.out.println("Not uploading the image"); 
			  }
	        // Construct the update clauses based on the keys in ContractMap
	       
		  if (ContractMap.containsKey("ServiceID")) {
			    updatestr += "ServiceID = " + ContractMap.get("ServiceID") + ",\r\n";
			}
			if (ContractMap.containsKey("UserID")) {
			    updatestr += "UserID = " + ContractMap.get("UserID") + ",\r\n";
			}
		  if (ContractMap.containsKey("ContactFirstName")) {
				updatestr +=" ContactFirstName = '" + ContractMap.get("ContactFirstName") + "',\r\n";
			}
		  if (ContractMap.containsKey("ContactLastName")) {
				updatestr += " ContactLastName =' " + ContractMap.get("ContactLastName") + "',\r\n";
			}
		  if (ContractMap.containsKey("UpdatedBy")) {
				updatestr += " UpdatedBy = " + ContractMap.get("UpdatedBy") + ",\r\n";
			}		  
		  if (ContractMap.containsKey("StartDate")) {
				updatestr += " StartDate = '" + ContractMap.get("StartDate") + "',\r\n";
			}
		  if (ContractMap.containsKey("EndDate")) {
				updatestr += " EndDate =' " + ContractMap.get("EndDate") + "',\r\n";
			}
		  if (ContractMap.containsKey("Fee")) {
				updatestr += " Fee = '" + ContractMap.get("Fee") + "',\r\n";
			}
		  if (ContractMap.containsKey("Currency")) {
				updatestr += " Currency = '" + ContractMap.get("Currency") + "',\r\n";
			}
		  
		  
		  if (ContractMap.containsKey("Status")) {
				updatestr += " Status = " + ContractMap.get("Status") + ",\r\n";
			}
		// Remove trailing comma from the update string
	        updatestr = updatestr.replaceAll(",$", "");

	        // Create a Hibernate session
	        Session session = HibernateUtil.buildSessionFactory();

	        // Begin a transaction
	        session.beginTransaction();

	        // Create a native SQL query to update servicecontractdetails table
	        Query query = session.createNativeQuery("UPDATE ServiceContractDetails SET " + updatestr + " WHERE ContractID = " + ContractID + ";");

	        int ret = 0;  // Variable to store the result of the update

	        try {
	            // Execute the update query
	            ret = query.executeUpdate();

	            // Log success message
	            Constant.log(" Updated servicecontractdetails table for ContractID = " + ContractID, 1);

	            // Commit the transaction
	            session.getTransaction().commit();
	        } catch (Exception ex) {
	            // Rollback the transaction if an exception occurs
	            session.getTransaction().rollback();
	        } finally {
	            // Uncomment the line below if you want to close the session here
	            // session.close();
	        }

	        // Return the result of the update
	        return ret;
	    }

	  public static int deleteContract(int ContractID) {
			
			Session session = HibernateUtil.buildSessionFactory();

			// creating session object
			//Session session = factory;
			// creating transaction object
			session.beginTransaction();
			
			 String updatestr = " status = 0  ";
			 
			 System.out.println(updatestr);
				Query query = session.createNativeQuery(
						"UPDATE `ServiceContractDetails`\r\n" + "SET\r\n" + updatestr + "WHERE `ContractID` = " + ContractID + ";");
				int ret = 0;
				try {
				ret = query.executeUpdate();
				session.getTransaction().commit();
				System.out.println("deleted entry for ContractID =  " + ContractID);
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return ret;
			 	 
		}

	// Method to retrieve a list of ServiceContracts based on ContractID
	    public static List<ServiceContract> getContract(int ContractID) {
	        // Create a Hibernate session
	        Session session = HibernateUtil.buildSessionFactory();

	        // Create a native SQL query to retrieve ServiceContract details
	        Query query1 = session.createNativeQuery("SELECT\r\n"
	        		+ "    c.ContractID,\r\n"
	        		+ "    c.ServiceID,\r\n"
	        		+ "    s.ServiceName,\r\n"
	        		+ "    r.first_name,\r\n"
	        		+ "    c.UserID,\r\n"
	        		+ "    c.ContactFirstName,\r\n"
	        		+ "    c.ContactLastName,\r\n"
	        		+ "    c.CreatedBy,\r\n"
	        		+ "    c.DocumentPath,\r\n"
	        		+ "    c.StartDate,\r\n"
	        		+ "    c.EndDate,\r\n"
	        		+ "    c.Fee,\r\n"
	        		+ "    c.Currency,\r\n"
	        		+ "    c.CreatedDate,\r\n"
	        		+ "    c.LastUpdatedDate,\r\n"
	        		+ "    c.Status,\r\n"
	        		+ "    c.UpdatedBy,\r\n"
	        		+ "    r.last_name\r\n"
	        		+ "FROM\r\n"
	        		+ "    ServiceContractDetails c\r\n"
	        		+ "JOIN\r\n"
	        		+ "    SponsoredServicesMaster s ON c.ServiceID = s.ServiceID\r\n"
	        		+ "LEFT JOIN\r\n"
	        		+ "    registration r ON c.UserID = r.registration_id\r\n"
	        		+ "WHERE\r\n"
	//        		+ "    c.ContractID = s.ServiceID AND \r\n"
	        		+ " ContractID =" + ContractID + ";");

	        // List to store the resulting ServiceContracts
	        List<ServiceContract> contractsList = new ArrayList<>();

	        // Retrieve the results as a list of Object arrays
	        List<Object[]> resultList = query1.getResultList();

	        // Log the number of Service Contracts retrieved
	        Constant.log("Executed Query and Got: " + resultList.size() + " Services Contracts back", 1);

	        // Iterate through each Object array and create ServiceContract objects
	        for (Object[] obj : resultList) {
	            ServiceContract contract = new ServiceContract();

	            // Set ContractId, ServiceId, UserId, etc. for each ServiceContract
	            contract.setContractId(obj[0] != null ? (Integer) obj[0] : 0);
	            contract.setServiceId(obj[1] != null ? (Integer) obj[1] : 0);
	            contract.setServiceName((String) obj[2] != null ? (String) obj[2] : "");
		    	contract.setUserName((String) obj[3] != null? ((String) obj[17] != null? (String) obj[3] + " " + (String) obj[17]: (String) obj[3]) : "");
		    	
	            contract.setUserId(obj[4] != null ? (Integer) obj[4] : 0);
	            contract.setContactFirstName((String) obj[5] != null ? (String) obj[5] : "");
	            contract.setContactLastName((String) obj[6] != null ? (String) obj[6] : "");
	            contract.setCreatedBy(obj[7] != null ? (Integer) obj[7] : 0);
	            contract.setDocumentPath((String) obj[8] != null ? (String) obj[8] : "");

	            // Convert obj[7] and obj[8] to String, assuming they are Date fields
	            contract.setStartDate((obj[9] != null ? obj[9].toString() : null));
	            contract.setEndDate((obj[10] != null ? obj[10].toString() : null));

	            // Convert obj[9] to String, assuming it's a DECIMAL field
	            contract.setFee((obj[11] != null ? obj[11].toString() : null));

	            contract.setCurrency((String) (obj[12] != null ? obj[12] : null));
	            contract.setCreatedDate((Timestamp) (obj[13] != null ? obj[13] : null));
	            contract.setLastUpdatedDate((Timestamp) (obj[14] != null ? obj[14] : null));
	            contract.setStatus(obj[15] != null ? (Integer) obj[15] : 0);
	            contract.setUpdatedBy(obj[16] != null ? (Integer) obj[16] : 0);

	            // Add the ServiceContract to the list
	            contractsList.add(contract);
	        }
	        // Return the list of ServiceContracts
	        return contractsList;
	    }

	
}
