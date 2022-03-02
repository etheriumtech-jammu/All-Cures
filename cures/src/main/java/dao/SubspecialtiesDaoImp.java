package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Subspecialties;
import util.HibernateUtil;

public class SubspecialtiesDaoImp {
	public static ArrayList<Subspecialties> findAllSubSpecialties () {





		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select sspl_name from subspecialties;");
		ArrayList<Subspecialties> list= (ArrayList<Subspecialties>) query.getResultList();
		System.out.println("result list SubSpl@@@@@@@@@@@@@"+list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}





}
