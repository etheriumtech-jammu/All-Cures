package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Hospital;

import util.HibernateUtil;

public class HospitalDaoImpl {

	public static ArrayList<Hospital> findAllHospital() {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select  hospital_affliated  from hospital;");
		ArrayList<Hospital> list = (ArrayList<Hospital>) query.getResultList();
		System.out.println("result list hospitalList@@@@@@@@@@@@@" + list);
		trans.commit();   ///session.close();;
		return list;
	}

}
