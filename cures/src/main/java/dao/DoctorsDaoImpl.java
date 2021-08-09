
package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Doctors;
import model.Registration;
import util.Constant;
import util.HibernateUtil;

public class DoctorsDaoImpl {
	public static void main(String[] args) {
		Doctors check = new Doctors();
		check = DoctorsDaoImpl.getAllDoctorsInfo(6);
		Constant.log(("main doctors dao values" + check.getCountry_code()), 1);
	}

	public static ArrayList<String> findAllDoctors() {

		Constant.log("Finding All Docs", 1);

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session
				.createNativeQuery("select prefix, docname_first, docname_middle , docname_last from doctors;");
		List<Doctors> list = (List<Doctors>) query.getResultList();

		Constant.log("Got Results", 1);

		String docname;
		ArrayList<String> docList = new ArrayList<String>();
		// List<Doctors> list =( List<Doctors>) query.getResultList();
		// Constant.log("Retrieving and displaying the updated details of users");
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();

			{
				Constant.log((Constant.PREFIX + obj[0]), 1);
				Constant.log((Constant.FIRST_NAME + obj[1]), 1);
				Constant.log((Constant.MIDDLE_NAME + obj[2]), 1);
				Constant.log((Constant.LAST_NAME + obj[3]), 1);
				if (obj[2] == null) {
					docname = obj[0] + " " + obj[1] + " " + obj[3];
				} else {
					docname = obj[0] + " " + obj[1] + " " + obj[2] + " " + obj[3];
				}
				docList.add(docname);
			}
		}
		Constant.log("Total Doctors Found in the Doctors List:" + docList.size(), 1);
		session.close();
		return docList;

	}

	public static Doctors getAllDoctorsInfo(int docid) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();
		Constant.log("In DoctorsDAO, Getting Doctors Info For:" + docid, 1);

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();
		// String HQL= "from doctors INNER JOIN FETCH hospital.hospital_affliated
		// where.";
		Query query = session
				.createNativeQuery("SELECT doctors.docid,doctors.prefix, doctors.docname_first,doctors.docname_middle, "
						+ "doctors.docname_last, "
						+ "doctors.gender, doctors.edu_training, hospital.hospital_affliated, doctors.board_certifcate, doctors.membership, doctors.awards, "
						+ "doctors.availibity_for_appointment, doctors.doctor_location, doctors.telephone_nos, "
						+ "specialties.spl_name, doctors.other_spls, doctors.address1, doctors.address2, city.cityname, "
						+ "doctors.over_allrating, doctors.email, doctors.waiting_time,  states.statename , countries.countryname "
						+ "FROM doctors , hospital , specialties, city, states, countries "
						+ "WHERE  doctors.hospital_affliated = hospital.hospitalid  and "
						+ "doctors.primary_spl = specialties.splid and doctors.city = city.citycode and doctors.state = states.codeid and "
						+ "doctors.country_code = countries.countrycodeid and " + "docid=" + docid + ";");

		// This should return in only 1 doctor so why the List?
		// We should be using query.getSingleResult()
		List<Doctors> docsList = (List<Doctors>) query.getResultList();
		Doctors doc = null;
		Iterator itr = docsList.iterator();
		Constant.log("Executed Query and Got:" + docsList.size() + " doctors back", 1);

		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			{
				doc = new Doctors();
				doc.setDocid((Integer) obj[0]);
				Constant.log("--Iterating DocId:" + doc.getDocid(), 1);
				doc.setPrefix((String) obj[1] != null ? (String) obj[1] : "");
				doc.setDocname_first((String) obj[2] != null ? (String) obj[2] : "");
				doc.setDocname_middle(obj[3] != null ? (String) obj[3] : "");
				doc.setDocname_last(obj[4] != null ? (String) obj[4] : "");
				doc.setGender(obj[5] != null ? (Integer) obj[5] : 0);
				doc.setEdu_training(obj[6] != null ? (String) obj[6] : "");
				doc.setHospital_affliated(obj[7] != null ? (String) obj[7] : "");
				doc.setBoard_certifcate(obj[8] != null ? (String) obj[8] : "");
				doc.setMembership(obj[9] != null ? (String) obj[9] : "");
				doc.setAwards(obj[10] != null ? (String) obj[10] : "");
				doc.setAvailibity_for_appointment(obj[11] != null ? (Date) obj[11] : null);
				doc.setDoctor_location(obj[12] != null ? (String) obj[12] : "");
				doc.setTelephone_nos(obj[13] != null ? (String) obj[13] : "");
				doc.setPrimary_spl(obj[14] != null ? (String) obj[14] : "");
				doc.setOther_spls(obj[15] != null ? (String) obj[15] : "");
				doc.setAddress1(obj[16] != null ? (String) obj[16] : "");
				doc.setAddress2(obj[17] != null ? (String) obj[17] : "");
				doc.setCityname(obj[18] != null ? (String) obj[18] : "");
				float overall = (float) (obj[19] != null ? (Float) obj[19] : 0.0);
				doc.setOver_allrating(overall);
				doc.setEmail(obj[20] != null ? (String) obj[20] : "");
				doc.setWaiting_time(obj[21] != null ? (Integer) obj[21] : null);
				doc.setStatename(obj[22] != null ? (String) obj[22] : "");
				doc.setCountry_code(obj[23] != null ? (String) obj[23] : "");
			}
			Constant.log("--Returning from DoctorsDao, Doc Object for ID:" + doc.getDocid(), 1);
		}
		session.close();
		return doc;
	}

	public static void saveDoctors(Integer docid, String f_name, String l_name, String email) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();
		Constant.log("Saving New Doctor with Firstname to DB:" + f_name, 0);

		// creating session object
		Session session = factory;
		Doctors doc = new Doctors();
		// Transaction trans = (Transaction) session.beginTransaction();

		session.getTransaction().begin();
		Doctors doctorFound = findDoctorsByEmail(email);
		if (null != doctorFound && null != doctorFound.getEmail()) {

			Query query = session
					.createNativeQuery("UPDATE  doctors " + " SET docid =" + docid + " WHERE email = '" + email + "';");
			int ret = 0;
			try {
				ret = query.executeUpdate();
				// trans.commit();
				System.out.println("updated doctors table for email =  " + email + " set docid=" + docid);
				session.getTransaction().commit();
				session.close();

			} catch (Exception e) {
				Constant.log(e.getStackTrace().toString(), 3);
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
		} else {
			try {
				Query query = session
						.createNativeQuery("insert into  doctors (docid, docname_first,docname_last,email) values ("
								+ docid + ",'" + f_name + "','" + l_name + "','" + email + "');");
				int ret = 0;
				ret = query.executeUpdate();
				// trans.commit();
				System.out.println("insert new doctor with email =  " + email + " and docid=" + docid);
				session.getTransaction().commit();
				session.close();
				// TODO: This implementation is wrong; Setting the RegistrationID as the Doctors
				// DocId;
//				doc.setDocid(docid);
//				doc.setPrefix(Constant.DR);
//				doc.setEmail(email);
//				doc.setDocname_first(f_name);
//				doc.setDocname_last(l_name);
//				session.update(doc);
//				session.getTransaction().commit();
//				session.close();
				// sessionFactory.close();

			} catch (Exception e) {
				Constant.log(e.getStackTrace().toString(), 3);
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
		}
	}

	public static Doctors findDoctorsByEmail(String email) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>" + email), 0);
		int docid = 0;

		Doctors doctors = null;
		Query query = session.createNativeQuery("select docid,email from doctors where email='" + email.trim() + "'");
		ArrayList<Doctors> list = (ArrayList<Doctors>) query.getResultList();
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			doctors = new Doctors();
			Constant.log(">>>>>>>>>>>>>>>>>>Doctors Found for Email:" + email, 1);
			Object[] obj = (Object[]) itr.next();
			{
				doctors.setDocid(obj[0] != null ? (Integer) obj[0] : -1);
				doctors.setEmail((String) obj[1]);
			}
		}
		session.close();
		return doctors;
	}
}
