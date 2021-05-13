package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.City;
import model.States;
import model.Subspecialties;
import util.HibernateUtil;

public class CityDaoImpl {

	public static void main(String[]args){
		ArrayList<City> city= new ArrayList<City>();
		CityDaoImpl cityImpl = new CityDaoImpl();
		city = cityImpl.findAllCity();
		System.out.println("worktill dao is done cityIMPL"+city);

	}




	public static ArrayList<City> findAllCity() {





		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select distinct(cityname) from city;");
		ArrayList<City> list= (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@"+list);


		return list;
	}
	public static ArrayList<City> findAllPincode() {





		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select pincode from city;");
		ArrayList<City> list= (ArrayList<City>) query.getResultList();
		System.out.println("result list City@@@@@@@@@@@@@"+list);


		return list;
	}








}
