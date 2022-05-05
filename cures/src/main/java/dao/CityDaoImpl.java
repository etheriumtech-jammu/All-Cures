package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import model.City;
import util.HibernateUtil;

@Component
public class CityDaoImpl {

	public static void main(String[] args) {
		ArrayList<City> city = new ArrayList<City>();
		CityDaoImpl cityImpl = new CityDaoImpl();
		city = cityImpl.findAllCity();
		System.out.println("worktill dao is done cityIMPL" + city);

	}

	public static ArrayList<City> findAllCity() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("select distinct(cityname) from city;");
		ArrayList<City> list = (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}

	public static ArrayList<City> findAllPincode() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("select pincode from city;");
		ArrayList<City> list = (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}

	public static List getAllCityDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("select   cityname,  pincode  from city;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			String cityname = (String) objects[0];
			String pincode = (String) objects[1];
			hm.put("Cityname", cityname);
			hm.put("Pincode", pincode);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}
	public static List getAllStateDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("select   statename,  pincode  from states;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			String statename = (String) objects[0];
			String pincode = (String) objects[1];
			hm.put("Statename", statename);
			hm.put("Pincode", pincode);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}
	
	public static List getAllDiseaseDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();


		Query query = session.createNativeQuery("select   dc_name,  dc_status,parent_dc_id  from disease_condition;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			String dc_name = (String) objects[0];
			Integer dc_status = (Integer) objects[1];
			Integer parent_dc_id = (Integer) objects[2];
			hm.put("dcname", dc_name);
			hm.put("dcstatus", dc_status);
			hm.put("parentdcid", parent_dc_id);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}
	
	
	public static List getAllMobileDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery("select   country_code,  mobile  from newsletter;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer country_code = (Integer) objects[0];
			String mobile = (String) ""+objects[1];
			hm.put("Country", country_code);
			hm.put("Mobile", mobile);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}
	public static Integer getNewsletterDetails(String mobile) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
//		session.beginTransaction();
		int ret = 0;
		Query query = session.createNativeQuery("select * from newsletter where mobile="+mobile+";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		if(null != results)  ret = results.size();
		return ret;//)
	}
 

}
