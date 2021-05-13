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
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("SELECT countryname FROM countries;");
		ArrayList<countries> list= (ArrayList<countries>) query.getResultList();
		System.out.println("result list countries@@@@@@@@@@@@@"+list);

		return list;
	}




}
