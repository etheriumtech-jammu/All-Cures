package model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class StandaloneRating {
	
	public static Float findByTargeTypeIds (){
		Targetbytype target = new Targetbytype();
		Float avg_rating =null;
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		//String HQL= "from doctors  INNER JOIN FETCH hospital.hospital_affliated where.";
		Query query = session.createNativeQuery("Select * from targetbytype;");
		List<Targetbytype> list= ( List<Targetbytype>) query.getResultList();
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();
			target.setTarget_type_id((Integer)obj[0]);
			target.setTarget_type_name((String)obj[1]);
			Query query1 = session.createNativeQuery("Select * from doctorsrating where unique(target_id) and target_type_id="+obj[1]+");");
			List<Doctorsrating> list1= ( List<Doctorsrating>) query.getResultList();
			Iterator itr1 = list.iterator();
			while(itr1.hasNext()){
				Object[] obj1 = (Object[]) itr1.next();
			Doctorsrating docrate= new Doctorsrating();
			docrate.setRate_id((Integer)obj[0]);
			docrate.setComments((String)obj[1]);
			docrate.setRatedBy_id((Integer) obj[2]);
			docrate.setRatedBy_type_id((Integer)obj[3]);
			docrate.setTarget_id((Integer)obj[4]);
			docrate.setTarget_type_id((Integer)obj[5]);
			docrate.setRatingVal((Float)obj[6]);
			Query query2 = session.createNativeQuery("Select avg(ratingVal) from doctorsrating where target_id="+obj[4]+";");
			List<Doctorsrating> list2= ( List<Doctorsrating>) query.getResultList();
			Iterator itr2 = list.iterator();
			while(itr2.hasNext()){
				Object[] obj2 = (Object[]) itr2.next();
			 avg_rating =(Float)obj2[0];
			}
		}
	}	
		session.getTransaction().commit();   //session.close();
		return avg_rating;

	}

}

