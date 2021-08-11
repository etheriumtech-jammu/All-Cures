package dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Doctors;
import model.Patient;
import util.Constant;
import util.HibernateUtil;

public class PatientDaoImpl {

	public static void savePatient(Integer patient_id,String f_name,String l_name, String email) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;
		Patient pat = new Patient();
		Constant.log("Saving New Patient with Firstname to DB:"+f_name, 0);

		try {
			session.getTransaction().begin();
			// doc.setPrefix("Dr.");
			pat.setPatient_id(patient_id);
			pat.setFirst_name(f_name);
			pat.setLast_name(l_name);
			pat.setEmail(email);
			session.save(pat);
			session.getTransaction().commit();
			session.close();
			// sessionFactory.close();
		} catch (Exception e) {
			Constant.log(e.getStackTrace().toString(), 3);
			session.getTransaction().rollback();
		}finally {
			session.close();
		}

	}
	public static Integer findAllPatientByPatientid( String email, String docfname, String doclname) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		Session session = factory;

		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();


		Query query = session.createNativeQuery("SELECT * FROM patient where email="+email+", first_name="+docfname+", last_name="+doclname+";");
		List<Doctors> list= ( List<Doctors>) query.getResultList();
		Patient patList = new Patient();
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();

			{
				patList.setPatient_id((Integer)obj[0]);
				patList.setFirst_name((String)obj[1]); 
				patList.setLast_name((String) obj[2]);
				patList.setAge((Integer) obj[3]);
				patList.setGender((Integer) obj[4]);
				patList.setDocid((Integer) obj[5]);
				patList.setEmail((String) obj[6]);

			}




		}
		int pi = patList.getPatient_id();
		session.close();
		return pi;

	}

}
