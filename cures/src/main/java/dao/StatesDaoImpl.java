package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.States;

import util.HibernateUtil;

public class StatesDaoImpl {


	public static ArrayList<States> findAllStates () {





		// creating seession factory object
		SessionFactory factory= HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select distinct statename  from states;");
		ArrayList<States> list= (ArrayList<States>) query.getResultList();
		System.out.println("result list StateList@@@@@@@@@@@@@"+list);

		return list;
	}






}
