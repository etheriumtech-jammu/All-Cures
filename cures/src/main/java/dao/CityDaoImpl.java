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
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select distinct(cityname) from city;");
		ArrayList<City> list = (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@" + list);
		session.getTransaction().commit();   //session.close();
		return list;
	}

	public static ArrayList<City> findAllPincode() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select pincode from city;");
		ArrayList<City> list = (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@" + list);
		session.getTransaction().commit();   //session.close();
		return list;
	}

	public static List getAllCityDetails() {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

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
		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}

}
