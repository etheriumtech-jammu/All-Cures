package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class SponsoredAdsDaoImpl {

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

	public static Integer InsertAdDetails( HashMap<String, Object> AdMap) {

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
			HashMap hm2=uploadFile(image);
			System.out.println(hm2);

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//		session.getTransaction().commit(); session.close();
		}

		return ret;
	}

	public static HashMap uploadFile( CommonsMultipartFile image) {
		
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
		String path = System.getProperty( "catalina.base" ) + "/webapps/"+ "cures_adsimages";
		System.out.println(path);
		// path = path+"/uitest";
		String filename = image.getOriginalFilename();
		
		// Rename the file to the new file name
        String renamedFilename = "Ad_" + res + "." + FilenameUtils.getExtension(filename);

		System.out.println(path + "/" + renamedFilename);
		try {
			byte barr[] = image.getBytes();

			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + renamedFilename));
			bout.write(barr);
			bout.flush();
			bout.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		Query query1 = session.createNativeQuery(
				"Update CampaignAds set ImageLocation=" + path + "/" + renamedFilename + " where AdID=" + res + ";");
		int ret = 0;
		try {
		ret = query1.executeUpdate();
		session.getTransaction().commit();
		}catch(Exception e)
		{
			session.getTransaction().rollback();
			
		}
		HashMap hm = new HashMap();
		hm.put("success", 1);
		
		return hm;
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
				 "SELECT ca.AdID, ca.CampaignID, cam.CampaignName, ca.DiseaseCondition, dc.dc_desc, ca.AdTypeID, at.AdTypeName,\r\n"
				 + "       ca.AdTitle, ca.AdDescription, ca.AdCount, ca.AdDelivered, ca.ImageLocation, ca.ImageAltText,\r\n"
				 + "       ca.StartDate, ca.EndDate, ca.CreateDate, ca.LastUpdatedDate, ca.ReviewStatus, ca.PaymentStatus\r\n"
				 + "FROM CampaignAds ca\r\n"
				 + "JOIN disease_condition dc ON ca.DiseaseCondition = dc.dc_id\r\n"
				 + "JOIN Campaign cam ON cam.CampaignID = ca.CampaignID\r\n"
				 + "JOIN AdsTypes at ON ca.AdTypeID = at.AdTypeID;\r\n"
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

			Integer AdTypeID = (Integer) objects[5];
			String AdTypeName=(String)objects[6];
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

			hm.put("AdID", AdID);
			hm.put("CampaignID", CampaignID);
			hm.put("CampaignName", CampaignName);
			hm.put("DiseaseConditionID", DiseaseConditionID);
			hm.put("DiseaseConditionName", DiseaseConditionName);
			hm.put("AdTypeID", AdTypeID);
			hm.put("AdTypeName", AdTypeName);
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


			hmFinal.add(hm);

		}

		return hmFinal;

	}

	public static List CampaignsAdsByID(int AdID) {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"SELECT ca.CampaignID, cam.CampaignName, ca.DiseaseCondition, dc.dc_desc, ca.AdTypeID, at.AdTypeName,\r\n"
				+ "       ca.AdTitle, ca.AdDescription, ca.AdCount, ca.AdDelivered, ca.ImageLocation, ca.ImageAltText,\r\n"
				+ "       ca.StartDate, ca.EndDate, ca.CreateDate, ca.LastUpdatedDate, ca.ReviewStatus, ca.PaymentStatus\r\n"
				+ "FROM CampaignAds ca\r\n"
				+ "JOIN disease_condition dc ON ca.DiseaseCondition = dc.dc_id\r\n"
				+ "JOIN Campaign cam ON cam.CampaignID = ca.CampaignID\r\n"
				+ "JOIN AdsTypes at ON ca.AdTypeID = at.AdTypeID\r\n"
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

			Integer AdTypeID = (Integer) objects[4];
			String AdTypeName=(String)objects[5];
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

			hm.put("CampaignID", CampaignID);
			hm.put("CampaignName", CampaignName);
			hm.put("DiseaseConditionID", DiseaseConditionID);
			hm.put("DiseaseConditionName", DiseaseConditionName);
			hm.put("AdTypeID", AdTypeID);
			hm.put("AdTypeName", AdTypeName);
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
		if (campaignAdsMap.containsKey("AdTypeID")) {
			updatestr += "`AdTypeID` = '" + campaignAdsMap.get("AdTypeID") + "',\r\n";
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
	public static List ListAdsSlotsTypes() {

	Session session = HibernateUtil.buildSessionFactory();

	Query query = session.createNativeQuery(
			"Select SlotTypeId, SlotTypeName from AdsSlotsTypes where TotalAvailableSlots-AllotedSlots>0;");
	List<Object[]> results = (List<Object[]>) query.getResultList();
	System.out.println(results.size());

	List<Object[]> arrayDataList = new ArrayList<>();
	for (Object[] objects : results) {
	    Integer SlotTypeId = (Integer) objects[0];
	    String SlotTypeName = (String) objects[1];

	    Object[] dataArray = new Object[]{SlotTypeId, SlotTypeName};
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
		
	public static List ListAdsTypes() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select AdTypeID,AdTypeName from AdsTypes where Status = 1 ;");
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
}