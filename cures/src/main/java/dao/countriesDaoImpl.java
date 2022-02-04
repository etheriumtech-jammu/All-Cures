package dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.Query;

import java.util.ArrayList;

import model.countries;
import util.HibernateUtil;;

public class countriesDaoImpl {


	public static ArrayList<countries> findAllCountries () {



		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("SELECT countryname FROM countries;");
		ArrayList<countries> list= (ArrayList<countries>) query.getResultList();
		System.out.println("result list countries@@@@@@@@@@@@@"+list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}




}
