package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Specialties;
import model.States;
import util.HibernateUtil;

public class SpecialtiesDaoImpl {
	public static ArrayList<Specialties> findAllSpecialties () {





		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select spl_name from specialties;");
		ArrayList<Specialties> list= (ArrayList<Specialties>) query.getResultList();
		System.out.println("result list Spl@@@@@@@@@@@@@"+list);
		session.close();
		return list;
	}









}
