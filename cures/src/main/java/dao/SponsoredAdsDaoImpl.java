package dao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class SponsoredAdsDaoImpl {
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
			
			hm.put("CompanyID", CompanyID);
			hm.put("CompanyName", CompanyName);
			hm.put("CompanyWebsite", CompanyWebsite);
			hm.put("ContactPerson", ContactPerson);
			hm.put("Email", Email);
			hm.put("Phone", Phone);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			

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
			
			hm.put("CampaignID", CampaignID);
			hm.put("CompanyID", CompanyID);
			hm.put("CampaignName", CampaignName);
			hm.put("StartDate", StartDate);
			hm.put("EndDate", EndDate);
			hm.put("CreateDate", CreateDate);
			hm.put("LastUpdatedDate", LastUpdatedDate);
			

			hmFinal.add(hm);

		}

		return hmFinal;

	}
	
	public static List CampaignsAds() {

		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"Select * from CampaignAds\r\n" + "\r\n" + "");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			Integer AdID= (Integer) objects[0];

			Integer CampaignID = (Integer) objects[1];

			Integer DiseaseCondition = (Integer) objects[2];

			Integer AdTypeID = (Integer) objects[3];

			String AdTitle = (String) objects[4];
			
			String AdDescription= (String) objects[5];

			Integer AdCount = (Integer) objects[6];

			Integer AdDelivered = (Integer) objects[7];

			String ImageLocation = (String) objects[8];

			String ImageAltText= (String) objects[9];
			Date StartDate = (Date) objects[10];

			Date EndDate = (Date) objects[11];
				
			Date CreateDate= (Date) objects[12];
			Date LastUpdatedDate= (Date) objects[13];
			
			Integer ReviewStatus= (Integer) objects[14];

			Integer PaymentStatus= (Integer) objects[15];

			hm.put("AdID", AdID);
			hm.put("CampaignID", CampaignID);
			hm.put("DiseaseCondition", DiseaseCondition);
			hm.put("AdTypeID", AdTypeID);
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
	
}
