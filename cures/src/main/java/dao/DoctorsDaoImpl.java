
package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Doctors;
import model.Registration;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.EnDeCryptor;
import util.HibernateUtil;

@Component
public class DoctorsDaoImpl {

	public static MemcachedClient mcc = null;

	public static void main(String[] args) {
		Doctors check = new Doctors();
		check = DoctorsDaoImpl.getAllDoctorsInfo(6);
		Constant.log(("main doctors dao values" + check.getCountry_code()), 1);
	}

	public static int updateProfile(HashMap profileMap) {
		int rowno = 0;
		String updatestr = "";
		if (profileMap.containsKey("rowno")) {
			rowno = (int) profileMap.get("rowno");
		}
		if (rowno == 0) {
			Constant.log("rowno not provided in request", 3);
			return 0;
		} // return error
		if (profileMap.containsKey("gender")) {
			updatestr += " gender = " + profileMap.get("gender") + ",\r\n";
		}
		if (profileMap.containsKey("edu_training")) {
			updatestr += " edu_training = '" + profileMap.get("edu_training") + "',\r\n";
		}
		if (profileMap.containsKey("insurance_accept")) {
			updatestr += " insurance_accept = " + profileMap.get("insurance_accept") + ",\r\n";
		}
		if (profileMap.containsKey("hospital_affliated")) {
			updatestr += " hospital_affliated = " + profileMap.get("hospital_affliated") + ",\r\n";
		}
		if (profileMap.containsKey("board_certifcate")) {
			updatestr += " board_certifcate = '" + profileMap.get("board_certifcate") + "',\r\n";
		}
		if (profileMap.containsKey("membership")) {
			updatestr += " membership = '" + profileMap.get("membership") + "',\r\n";
		}
		if (profileMap.containsKey("awards")) {
			updatestr += " awards = '" + profileMap.get("awards") + "',\r\n";
		}
		if (profileMap.containsKey("availibity_for_appointment")) {
			updatestr += " availibity_for_appointment = " + profileMap.get("availibity_for_appointment") + ",\r\n";
		}
		if (profileMap.containsKey("doctor_location")) {
			updatestr += " doctor_location = '" + profileMap.get("doctor_location") + "',\r\n";
		}
		if (profileMap.containsKey("telephone_nos")) {
			updatestr += " telephone_nos = '" + profileMap.get("telephone_nos") + "',\r\n";
		}
		if (profileMap.containsKey("primary_spl")) {
			updatestr += " primary_spl = " + profileMap.get("primary_spl") + ",\r\n";
		}
		if (profileMap.containsKey("other_spls")) {
			updatestr += " other_spls = '" + profileMap.get("other_spls") + "',\r\n";
		}
		if (profileMap.containsKey("sub_spls")) {
			updatestr += " sub_spls = " + profileMap.get("sub_spls") + ",\r\n";
		}
		if (profileMap.containsKey("address1")) {
			updatestr += " address1 = '" + profileMap.get("address1") + "',\r\n";
		}
		if (profileMap.containsKey("address2")) {
			updatestr += " address2 = '" + profileMap.get("address2") + "',\r\n";
		}
		if (profileMap.containsKey("city")) {
			updatestr += " city = " + profileMap.get("city") + ",\r\n";
		}
		if (profileMap.containsKey("state")) {
			updatestr += " state = " + profileMap.get("state") + ",\r\n";
		}
		if (profileMap.containsKey("country_code")) {
			updatestr += " country_code = " + profileMap.get("country_code") + ",\r\n";
		}
		if (profileMap.containsKey("over_allrating")) {
			updatestr += " over_allrating = " + profileMap.get("over_allrating") + ",\r\n";
		}
		if (profileMap.containsKey("create_date")) {
			updatestr += " create_date = " + profileMap.get("create_date") + ",\r\n";
		}
		if (profileMap.containsKey("prefix")) {
			updatestr += " prefix = '" + profileMap.get("prefix") + "',\r\n";
		}
		if (profileMap.containsKey("docname_first")) {
			updatestr += " docname_first = '" + profileMap.get("docname_first") + "',\r\n";
		}
		if (profileMap.containsKey("docname_middle")) {
			updatestr += " docname_middle = '" + profileMap.get("docname_middle") + "',\r\n";
		}
		if (profileMap.containsKey("docname_last")) {
			updatestr += " docname_last = '" + profileMap.get("docname_last") + "',\r\n";
		}
		if (profileMap.containsKey("email")) {
			updatestr += " email = '" + profileMap.get("email") + "',\r\n";
		}
		if (profileMap.containsKey("waiting_time")) {
			updatestr += " waiting_time = " + profileMap.get("waiting_time") + ",\r\n";
		}
		if (profileMap.containsKey("hospitalid")) {
			updatestr += " hospitalid = " + profileMap.get("hospitalid") + ",\r\n";
		}
		if (profileMap.containsKey("hospital_affliated_hospitalid")) {
			updatestr += " hospital_affliated_hospitalid = " + profileMap.get("hospital_affliated_hospitalid")
					+ ",\r\n";
		}
		if (profileMap.containsKey("statename_codeid")) {
			updatestr += " statename_codeid = " + profileMap.get("statename_codeid") + ",\r\n";
		}
		if (profileMap.containsKey("verified")) {
			updatestr += " verified = " + profileMap.get("verified") + ",\r\n";
		}
		if (profileMap.containsKey("uprn")) {
			updatestr += " uprn = " + profileMap.get("uprn") + ",\r\n";
		}
		if (profileMap.containsKey("registration_number")) {
			updatestr += " registration_number = " + profileMap.get("registration_number") + ",\r\n";
		}
		if (profileMap.containsKey("about")) {
			updatestr += " about = '" + profileMap.get("about") + "',\r\n";
		}
		if (profileMap.containsKey("website_url")) {
			updatestr += " website_url = '" + profileMap.get("website_url") + "',\r\n";
		}

		updatestr = updatestr.replaceAll(",$", "");
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		Query query = session
				.createNativeQuery("UPDATE doctors " + "SET " + updatestr + " WHERE rowno = " + rowno + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("updated doctors table for rowno =  " + rowno);
			Constant.log(">>>>>>>>>>>>>>>>>>updated doctors table for rowno =  " + rowno, 1);
//			int check = new DoctorsDaoImpl().memcacheUpdateDoctor(docid);
			if (mcc == null)
				new DoctorsDaoImpl().initializeCacheClient();
			// Remove the Doctor Found to the Cache since the same ID will be updated in
			// next fetch
			// mcc.replace(Constant.DOCID + "_" + docid, 360000, jsondata).getStatus();
			mcc.delete(Constant.ROWNO + "_" + rowno);
			session.getTransaction().commit();

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}
		return ret;
	}

//	public static int memcacheUpdateDoctor(int docid) {
//		// String id=request.getParameter("docid");
//		Constant.log("Update memache Req for Profile For DocID: " + docid, 1);
//		DoctorsDaoImpl doctorDao = null;
//		Doctors doctorObj = null;
//		doctorDao = new DoctorsDaoImpl();
//		Constant.log("Got Null From MemCache on the Doc:" + docid, 1);
//		doctorObj = doctorDao.getAllDoctorsInfo(docid);
//		if (mcc == null)
//			doctorDao.initializeCacheClient();
//		// Add the Doctor Found to the Cache since the ID was not there
//		Gson gson = new GsonBuilder().serializeNulls().create();
//		String jsondata = gson.toJson(doctorObj);
//		//mcc.replace(Constant.DOCID + "_" + docid, 360000, jsondata).getStatus();
//		//mcc.replace(Constant.DOCID + "_" + docid, 360000, jsondata).getStatus();
//		mcc.delete(Constant.DOCID + "_" + docid);
//		return docid;
//	}

	public MemcachedClient initializeCacheClient() {
		try {
			Constant.log("Trying Connection to Memcache server", 0);
			mcc = new MemcachedClient(
					new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
					AddrUtil.getAddresses(Constant.ADDRESS));
			Constant.log("Connection to Memcache server Sucessful", 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constant.log("Connection to Memcache server UN-Sucessful", 3);
		}
		return mcc;
	}

	public String findDocInCache(int rowno) {
		String cacheString = null;
		
		// This is the ADDRESS OF MEMCACHE
		// TODO: Move to a Config Entry in Web.xml
		if (mcc == null) {
			initializeCacheClient();
		}
		Constant.log("Getting docid from MemCache", 0);
		if (mcc.get(Constant.ROWNO + "_" + rowno) != null)
			cacheString = mcc.get(Constant.ROWNO + "_" + rowno).toString();
		Constant.log("Found In MemCache:" + cacheString, 0);
		return cacheString;
	}

	public static int verifyDoctor(HashMap doctorsMap) {
		String updatestr = "";
		String updatestrPassword = "";
		String email = "";
		if (doctorsMap.containsKey("email")) {
			email = (String) doctorsMap.get("email");
		}
		if (doctorsMap.containsKey("uprn")) {
			updatestr += " uprn = " + doctorsMap.get("uprn") + ",\r\n";
		}
		if (doctorsMap.containsKey("registration_number")) {
			updatestr += " registration_number = " + doctorsMap.get("registration_number") + ",\r\n";
		}
		if (doctorsMap.containsKey("stateid")) {
			updatestr += " statename_codeid = " + doctorsMap.get("stateid") + ",\r\n";
		}
		if (doctorsMap.containsKey("verified")) {
			updatestr += " verified = " + doctorsMap.get("verified") + ",\r\n";
		}
		updatestr = updatestr.replaceAll(",$", "");
		String hashedPass = null;
		if (doctorsMap.containsKey("password")) {
			updatestrPassword = (String) doctorsMap.get("password");
			EnDeCryptor encrypt = new EnDeCryptor();
			final String secretKey = Constant.SECRETE;
			hashedPass = encrypt.encrypt(updatestrPassword, secretKey);
		}
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		Query query = session
				.createNativeQuery("UPDATE doctors " + "SET " + updatestr + " WHERE email = '" + email + "';");
		Query queryPassword = session.createNativeQuery(
				"UPDATE registration " + "SET pass_word='" + hashedPass + "' WHERE email_address = '" + email + "';");
		int ret = 0;
		int docid = 0;

		try {
			ret = query.executeUpdate();
			ret = queryPassword.executeUpdate();
			System.out.println("updated doctors table for email =  " + email);
//			Session session2 = factory;
//			// creating transaction object
//			Transaction trans2 = (Transaction) session.beginTransaction();
			// prepare to return docid using email id
			Query queryDocId = session.createNativeQuery("select docid from doctors WHERE email = '" + email + "';");
			ArrayList list = (ArrayList) queryDocId.getResultList();
			docid = (list.get(0) != null ? (Integer) list.get(0) : 0);
			Constant.log(">>>>>>>>>>>>>>>>>>User Found for EMAILID:" + email + " docid=" + docid, 1);
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return docid;
	}

	public static ArrayList<String> findAllDoctors() {

		Constant.log("Finding All Docs", 1);

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

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
//		session.getTransaction().commit();   //session.close();
		return docList;

	}

	public static Doctors getAllDoctorsInfo(int rowno) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		Constant.log("In DoctorsDAO, Getting Doctors Info For:" + rowno, 1);
		
		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		// String HQL= "from doctors INNER JOIN FETCH hospital.hospital_affliated
		// where.";
		Query query = session
				.createNativeQuery("SELECT doctors.docid,doctors.prefix, doctors.docname_first,doctors.docname_middle, "
						+ "doctors.docname_last, "
						+ "doctors.gender, doctors.edu_training, hospital.hospital_affliated, doctors.board_certifcate, doctors.membership, doctors.awards, "
						+ "doctors.availibity_for_appointment, doctors.doctor_location, doctors.telephone_nos, "
						+ "specialties.spl_name, doctors.other_spls, doctors.address1, doctors.address2, city.cityname, "
						+ "doctors.over_allrating, doctors.email, doctors.waiting_time,  states.statename , countries.countryname, "
						+ "doctors.primary_spl, doctors.sub_spls, doctors.about, doctors.city, doctors.state, doctors.country_code,doctors.hospital_affliated as hospital_affliated_code, "
						+ "doctors.rowno, doctors.website_url,doctors.subscription  "
						+ "FROM doctors , hospital , specialties, city, states, countries "
						+ "WHERE  doctors.hospital_affliated = hospital.hospitalid  and "
						+ "doctors.primary_spl = specialties.splid and doctors.city = city.citycode and doctors.state = states.codeid and "
						+ "doctors.country_code = countries.countrycodeid and " + "rowno=" + rowno + ";");

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
				//doc.setDocid((Integer) obj[0]);
				doc.setDocid(obj[0] != null ? (Integer) obj[0] : 0);
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
				doc.setWaiting_time(obj[21] != null ? (Integer) obj[21] : 0);
				doc.setStatename(obj[22] != null ? (String) obj[22] : "");
				doc.setCountry_code(obj[23] != null ? (String) obj[23] : "");

				doc.setPrimary_spl_code(obj[24] != null ? (Integer) obj[24] : 0);
				// doc.setOther_spls_code(obj[25] != null ? (Integer) obj[25] : 0);
				doc.setSub_spls_code(obj[25] != null ? (Integer) obj[25] : 0);
				doc.setAbout(obj[26] != null ? (String) obj[26] : "");
				doc.setCity_code(obj[27] != null ? (Integer) obj[27] : 0);
				doc.setState_code(obj[28] != null ? (Integer) obj[28] : 0);
				doc.setCountries_code(obj[29] != null ? (Integer) obj[29] : 0);
				doc.setHospital_affliated_code(obj[30] != null ? (Integer) obj[30] : 0);
				doc.setRowno((long) (obj[31] != null ? (Integer) obj[31] : 0));
				doc.setWebsite_url((String) (obj[32]));
				doc.setSubscription(obj[33] != null ? (Integer) obj[33] : 0);
			}
			Constant.log("--Returning from DoctorsDao, Doc Object for ID:" + doc.getDocid() +" rowno:" + doc.getRowno(), 1);
		}
//		session.getTransaction().commit();   //session.close();
		return doc;
	}	
	
	public static Doctors getAllDoctorsInfoByDocId(int docid) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		Constant.log("In DoctorsDAO, Getting Doctors Info For:" + docid, 1);

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		// String HQL= "from doctors INNER JOIN FETCH hospital.hospital_affliated
		// where.";
		Query query = session
				.createNativeQuery("SELECT doctors.docid,doctors.prefix, doctors.docname_first,doctors.docname_middle, "
						+ "doctors.docname_last, "
						+ "doctors.gender, doctors.edu_training, hospital.hospital_affliated, doctors.board_certifcate, doctors.membership, doctors.awards, "
						+ "doctors.availibity_for_appointment, doctors.doctor_location, doctors.telephone_nos, "
						+ "specialties.spl_name, doctors.other_spls, doctors.address1, doctors.address2, city.cityname, "
						+ "doctors.over_allrating, doctors.email, doctors.waiting_time,  states.statename , countries.countryname, "
						+ "doctors.primary_spl, doctors.sub_spls, doctors.about, doctors.city, doctors.state, doctors.country_code,doctors.hospital_affliated as hospital_affliated_code, "
						+ "doctors.rowno, doctors.website_url,doctors.subscription  "
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
				//doc.setDocid((Integer) obj[0]);
				doc.setDocid(obj[0] != null ? (Integer) obj[0] : 0);
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
				doc.setWaiting_time(obj[21] != null ? (Integer) obj[21] : 0);
				doc.setStatename(obj[22] != null ? (String) obj[22] : "");
				doc.setCountry_code(obj[23] != null ? (String) obj[23] : "");

				doc.setPrimary_spl_code(obj[24] != null ? (Integer) obj[24] : 0);
				// doc.setOther_spls_code(obj[25] != null ? (Integer) obj[25] : 0);
				doc.setSub_spls_code(obj[25] != null ? (Integer) obj[25] : 0);
				doc.setAbout(obj[26] != null ? (String) obj[26] : "");
				doc.setCity_code(obj[27] != null ? (Integer) obj[27] : 0);
				doc.setState_code(obj[28] != null ? (Integer) obj[28] : 0);
				doc.setCountries_code(obj[29] != null ? (Integer) obj[29] : 0);
				doc.setHospital_affliated_code(obj[30] != null ? (Integer) obj[30] : 0);
				doc.setRowno((long) (obj[31] != null ? (Integer) obj[31] : 0));
				doc.setWebsite_url((String) (obj[32]));
				doc.setSubscription(obj[33] != null ? (Integer) obj[33] : 0);
			}
			Constant.log("--Returning from DoctorsDao, Doc Object for ID:" + doc.getDocid() +" rowno:" + doc.getRowno(), 1);
		}
//		session.getTransaction().commit();   //session.close();
		return doc;
	}

	public static void saveDoctors(Integer docid, String f_name, String l_name, String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		Constant.log("Saving New Doctor with Firstname to DB:" + f_name, 0);

		// creating session object
		//Session session = factory;
		session.beginTransaction();

		Doctors doc = new Doctors();
		// session.beginTransaction();

		//session.getTransaction().begin();
		Doctors doctorFound = findDoctorsByEmail(email);
		if (null != doctorFound && null != doctorFound.getEmail()) {

			Query query = session
					.createNativeQuery("UPDATE  doctors " + " SET docid =" + docid + " WHERE email = '" + email + "';");
			int ret = 0;
			try {
				ret = query.executeUpdate();
				// session.getTransaction().commit();
				System.out.println("updated doctors table for email =  " + email + " set docid=" + docid);
//				session.getTransaction().commit();
				session.getTransaction().commit();   //session.close();

			} catch (Exception e) {
				Constant.log(e.getStackTrace().toString(), 3);
				session.getTransaction().commit(); //session.getTransaction().rollback();
			} finally {
//				session.getTransaction().commit();   //session.close();
			}
		} else {
			try {
				Query query = session
						.createNativeQuery("insert into  doctors (docid, docname_first,docname_last,email) values ("
								+ docid + ",'" + f_name + "','" + l_name + "','" + email + "');");
				int ret = 0;
				ret = query.executeUpdate();
				// session.getTransaction().commit();
				System.out.println("insert new doctor with email =  " + email + " and docid=" + docid);
//				session.getTransaction().commit();
				session.getTransaction().commit();   //session.close();
				// TODO: This implementation is wrong; Setting the RegistrationID as the Doctors
				// DocId;
//				doc.setDocid(docid);
//				doc.setPrefix(Constant.DR);
//				doc.setEmail(email);
//				doc.setDocname_first(f_name);
//				doc.setDocname_last(l_name);
//				session.update(doc);
//				session.getTransaction().commit();
//				session.getTransaction().commit();   //session.close();
				// sessionFactory.close();

			} catch (Exception e) {
				Constant.log(e.getStackTrace().toString(), 3);
				session.getTransaction().commit(); //session.getTransaction().rollback();
			} finally {
//				session.getTransaction().commit();   //session.close();
			}
		}
	}

	public static Doctors findDoctorsByEmail(String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>" + email), 0);
		int docid = 0;

		Doctors doctors = null;
		Query query = session.createNativeQuery("select docid,email,rowno from doctors where email='" + email.trim() + "'");
		ArrayList<Doctors> list = (ArrayList<Doctors>) query.getResultList();
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			doctors = new Doctors();
			Constant.log(">>>>>>>>>>>>>>>>>>Doctors Found for Email:" + email, 1);
			Object[] obj = (Object[]) itr.next();
			{
				doctors.setDocid(obj[0] != null ? (Integer) obj[0] : -1);
				doctors.setEmail((String) obj[1]);
				doctors.setRowno(obj[2] != null ? (Long) Long.parseLong(""+obj[2]) : -1);

			}
		}
//		session.getTransaction().commit();   //session.close();
		return doctors;
	}
}
