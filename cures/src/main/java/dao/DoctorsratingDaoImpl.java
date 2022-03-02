package dao;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Doctorsrating;
import util.HibernateUtil;

public class DoctorsratingDaoImpl {
	public static void main(String[]args){
		Float check = null;
		check=DoctorsratingDaoImpl.getAllDoctorsOverallratingInfo(856);
		System.out.println("values"+check);
	}


	public static Float getAllDoctorsOverallratingInfo(int id) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		int index=0;
		Query query = session.createNativeQuery("select avg(ratingVal) from doctorsrating where target_id ="+id+";");
		String list=  query.getResultList().toString();
		list = list.replaceAll("[\\[\\]]", "");
		//list = list.replaceAll("]", " ");
		Float docFrating = Float.parseFloat(list) ;
		//  ArrayList<String>docList =new ArrayList<String>();
		//List<Doctors> list =( List<Doctors>) query.getResultList();
		//System.out.println("Retrieving and displaying the updated details of users");

		return docFrating;  

	}

	public static Doctorsrating getAllDoctorsDetailratingInfo(int id) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		int index=0;
		Query query = session.createNativeQuery("SELECT * FROM doctorsrating where target_id="+id+" order by  rate_id desc limit  0,3 ;");
		List<Doctorsrating> list=  query.getResultList();
		Doctorsrating docrating = new Doctorsrating();

		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();

			{
				docrating.setRate_id((Integer)obj[0]);
				docrating.setComments((String) obj[1]);
				docrating.setRatedBy_id((Integer) obj[2]);  
				docrating.setRatedBy_type_id((Integer)obj[3]);
				docrating.setTarget_id((Integer)obj[4]);
				docrating.setTarget_type_id((Integer)obj[5]);
				docrating.setRatingVal((Float)obj[6]);
			}
		}
//		session.getTransaction().commit();   //session.close();
		return docrating;  

	}

	public static int saveNewRating(Integer ratedbyid, Integer ratedbytype,Integer targetid, 
			Integer targetTypeid, Float ratingval){
		
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
//		//session.getTransaction().begin();
		session.beginTransaction();

		Doctorsrating docrate= new Doctorsrating();
		int value= 0;
		try{
			docrate.setRatedBy_id(ratedbyid);
//			docrate.setRatedBy_type_id(ratedbytype);
			docrate.setTarget_id(targetid);
			docrate.setTarget_type_id(targetTypeid); 
			docrate.setRatingVal(ratingval);// case when rating done non registered user ie. not logged-in user
			session.save(docrate);
//			session.getTransaction().commit();

			session.getTransaction().commit();   //session.close();
			value = 1;//"Success";
		}catch (Exception e) {
			// TODO: handle exception
			session.getTransaction().commit(); //session.getTransaction().rollback();
			value = 0;//"error";
		}finally {
//			session.getTransaction().commit();   //session.close();
		}
		return value;
		
		
		
	}	public static int saveNewComment(String comments, Integer ratedbyid, Integer ratedbytype,Integer targetid, 
			Integer targetTypeid){
		
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		session.beginTransaction();

//		//session.getTransaction().begin();
		Doctorsrating docrate= new Doctorsrating();
		int value= 0;
		try{
			docrate.setComments(comments);
			docrate.setRatedBy_id(ratedbyid);
//			docrate.setRatedBy_type_id(ratedbytype);
			docrate.setTarget_id(targetid);
			docrate.setTarget_type_id(targetTypeid);
			session.save(docrate);
//			session.getTransaction().commit();

			session.getTransaction().commit();   //session.close();
			value = 1;//"Success";
		}catch (Exception e) {
			// TODO: handle exception
//			session.getTransaction().commit(); //session.getTransaction().rollback();
			session.getTransaction().rollback();
			value = 0;//"error";
		}finally {
//			session.getTransaction().commit();   //session.close();
		}
		return value;
		
		
		
	}

}
