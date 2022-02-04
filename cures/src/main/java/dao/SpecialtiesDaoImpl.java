package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();

		Query query = session.createNativeQuery("select spl_name from specialties;");
		ArrayList<Specialties> list= (ArrayList<Specialties>) query.getResultList();
		System.out.println("result list Spl@@@@@@@@@@@@@"+list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}
	
	
	public static List getSpecialitySubSplMappingData(int splid) {
		List hmFinal = new ArrayList();

//		// creating seession factory object
//		Session session = HibernateUtil.buildSessionFactory();
//
//		// creating session object
//		//Session session = factory;
//
//		// creating transaction object
//		session.beginTransaction();
//		String splid_str = splid != null ? (int) splid: 0;
//		if (splid != null ) {
//			String spl_str = " and spl.splid=" +splid;
//		}
//		Query query = session.createNativeQuery("SELECT spl.splid, spl.spl_name, sub.ssplid, sub.sspl_name FROM  specialties spl\r\n"
//				+ "	inner join splsubsplmap map on spl.splid = map.splid\r\n"
//				+ "	inner join subspecialties sub on sub.ssplid = map.ssplid\r\n"
//				+ "	where sub.ssplactive =1 and spl.spl_active = 1 " + spl_str + " ;") ;
//
//		List<Object[]> results = (List<Object[]>) query.getResultList();
//		List hmFinal = new ArrayList();
//		for (Object[] objects : results) {
//			HashMap hm = new HashMap();
//			String cityname = (String) objects[0];
//			String pincode = (String) objects[1];
//			hm.put("Cityname", cityname);
//			hm.put("Pincode", pincode);
//			hmFinal.add(hm);
//		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;
	}








}
