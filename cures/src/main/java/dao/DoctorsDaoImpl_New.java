
package dao;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.AvailabilitySchedule;
import model.Doctor_New;
import model.Doctors;
import model.Registration;
import model.ServiceContract;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.EnDeCryptor;
import util.HibernateUtil;

@Component
public class DoctorsDaoImpl_New {

	public static MemcachedClient mcc = null;

	public static void main(String[] args) {
		Doctors check = new Doctors();
		check = DoctorsDaoImpl.getAllDoctorsInfo(6);
		Constant.log(("main doctors dao values" + check.getCountry_code()), 1);
	}

	public static int updateProfile(HashMap profileMap) {
		
		int DocID = (int) profileMap.get("DocID");
		String updatestr = "";
		
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
		
		if (profileMap.containsKey("awards")) {
			updatestr += " awards = '" + profileMap.get("awards") + "',\r\n";
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
		
		if (profileMap.containsKey("over_allrating")) {
			updatestr += " over_allrating = " + profileMap.get("over_allrating") + ",\r\n";
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
		
		if (profileMap.containsKey("verified")) {
			updatestr += " verified = " + profileMap.get("verified") + ",\r\n";
		}
		if (profileMap.containsKey("about")) {
			updatestr += " about = '" + profileMap.get("about") + "',\r\n";
		}
		if (profileMap.containsKey("website_url")) {
			updatestr += " website_url = '" + profileMap.get("website_url") + "',\r\n";
		}
		if (profileMap.containsKey("featured_doctor_date")) {
			updatestr += " featured_doctor_date = '" + profileMap.get("featured_doctor_date") + "',\r\n";
		}
		if (profileMap.containsKey("RegWithStateBoardID")) {
			updatestr += " RegWithStateBoardID = " + profileMap.get("RegWithStateBoardID") + ",\r\n";
		}
		if (profileMap.containsKey("NatlRegNo")) {
			updatestr += " NatlRegNo = '" + profileMap.get("NatlRegNo") + "',\r\n";
		}
		if (profileMap.containsKey("Natl_Reg_Date")) {
			updatestr += " Natl_Reg_Date = '" + profileMap.get("Natl_Reg_Date") + "',\r\n";
		}
		if (profileMap.containsKey("MedicineTypeID")) {
			updatestr += " MedicineTypeID = " + profileMap.get("MedicineTypeID") + ",\r\n";
		}
		if (profileMap.containsKey("Status")) {
			updatestr += " Status = " + profileMap.get("Status") + ",\r\n";
		}
		if (profileMap.containsKey("UpdatedBy")) {
			updatestr += " UpdatedBy = " + profileMap.get("UpdatedBy") + ",\r\n";
		}
		updatestr = updatestr.replaceAll(",$", "");
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		Query query = session
				.createNativeQuery("UPDATE Doctors_New " + "SET " + updatestr + " WHERE docid = " + DocID + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
//			System.out.println("updated doctors table for DocID =  " + DocID);
			Constant.log(">>>>>>>>>>>>>>>>>>updated doctors table for DocID =  " + DocID, 1);
//			int check = new DoctorsDaoImpl().memcacheUpdateDoctor(docid);
			if (mcc == null)
				new DoctorsDaoImpl_New().initializeCacheClient();
			// Remove the Doctor Found to the Cache since the same ID will be updated in
			// next fetch
			// mcc.replace(Constant.DOCID + "_" + docid, 360000, jsondata).getStatus();
			mcc.delete(Constant.DOCID + "_" + DocID);
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
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

	public String findDocInCache(int DocID) {
		String cacheString = null;

		// This is the ADDRESS OF MEMCACHE
		// TODO: Move to a Config Entry in Web.xml
		if (mcc == null) {
			initializeCacheClient();
		}
		Constant.log("Getting docid from MemCache", 0);
		if (mcc.get(Constant.DOCID + "_" + DocID) != null)
			cacheString = mcc.get(Constant.DOCID + "_" + DocID).toString();
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
		//		Constant.log((Constant.PREFIX + obj[0]), 1);
		//		Constant.log((Constant.FIRST_NAME + obj[1]), 1);
		//		Constant.log((Constant.MIDDLE_NAME + obj[2]), 1);
		//		Constant.log((Constant.LAST_NAME + obj[3]), 1);
				
				if (obj[2] == null) {
					if(obj[3]== null)
						docname = obj[0] + " " + obj[1] ;
					else
					docname = obj[0] + " " + obj[1] + " " + obj[3];	
				} else{
					if(obj[3]== null)
					docname = obj[0] + " " + obj[1] + " " + obj[2];
					else
					docname = obj[0] + " " + obj[1] + " " + obj[2] + " " + obj[3];	
				}
				docList.add(docname);
			}
		}
		Constant.log("Total Doctors Found in the Doctors List:" + docList.size(), 1);
//		session.getTransaction().commit();   //session.close();
		return docList;

	}

	public static Doctors getAllDoctorsInfo(int DocID) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		Constant.log("In DoctorsDAO, Getting Doctors Info For:" + DocID, 1);
		System.out.println("hhhhhhh");
		Query query1 = session.createNativeQuery("Select docid,docname_first from doctors doc inner JOin orders  on doc.docid=orders.user_id where docid is not null and End_date >= now();");
		Integer subscription=0;
		List<Object[]> results = (List<Object[]>) query1.getResultList();
		List hmFinal = new ArrayList();
		
		for (Object[] objects : results) {
		
			if ((Integer)objects[0] ==  DocID)
			{
				System.out.println("hello");
				subscription=1;
				
			}
		}
		Constant.log("Subscription id=" + subscription,1);
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
						+ "doctors.website_url,doctors.subscription,doctors.insurance_accept"
						+ "FROM doctors , hospital , specialties, city, states, countries "
						+ "WHERE  doctors.hospital_affliated = hospital.hospitalid  and "
						+ "doctors.primary_spl = specialties.splid and doctors.city = city.citycode and doctors.state = states.codeid and "
						+ "doctors.country_code = countries.countrycodeid and " + "DocID=" + DocID + ";");

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
	//			doc.setRowno((long) (obj[31] != null ? (Integer) obj[31] : 0));
				doc.setWebsite_url((String) (obj[32]));
				doc.setSubscription(obj[33] != null ? (Integer) obj[33] : 0);
			}
			Constant.log("--Returning from DoctorsDao, Doc Object for ID:" + doc.getDocid()+""  , 1);
		}
//		session.getTransaction().commit();   //session.close();
		return doc;
	}	
	
//	public static Doctors A(int docid) {
		// creating seession factory object
	public static Doctor_New getAllDoctorsInfoByDocId(int docid) {
		Session session = HibernateUtil.buildSessionFactory();
		
		Constant.log("In DoctorsDAO, Getting Doctors Info For:" + docid, 1);
		
		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		// String HQL= "from doctors INNER JOIN FETCH hospital.hospital_affliated
		// where.";
		Query query = session
				.createNativeQuery("SELECT\r\n"
						+ "    doctors.docid, doctors.gender, doctors.insurance_accept,\r\n"
						+ "    doctors.awards, doctors.telephone_nos, doctors.other_spls, doctors.over_allrating,\r\n"
						+ "    doctors.prefix, doctors.docname_first, doctors.docname_middle, doctors.docname_last,\r\n"
						+ "    doctors.email, doctors.waiting_time, doctors.verified, doctors.about, doctors.docactive,\r\n"
						+ "    doctors.website_url, doctors.featured_doctor_date, doctors.img_loc, doctors.Natl_Reg_Date,\r\n"
						+ "    doctors.NatlRegNo, doctors.CreatedDate, doctors.CreatedBy, doctors.Status,\r\n"
						+ "    doctors.LastUpdatedDate, doctors.UpdatedBy, h.hospital_affliated, s.spl_name,\r\n"
						+ "    st.statename AS state_name, mt.name AS MedicineTypeName,\r\n"
						+ "    da.Address1, da.Address2, c.cityname, states.statename AS address_state,\r\n"
						+ "    co.countryname AS address_country, mat.AddressType,\r\n"
						+ "    mdd.DegDesc, dd.YearOfGrad, mun.UnivName,\r\n"
						+ "    uc.cityname AS univ_city, us.statename AS univ_state, uco.countryname AS univ_country\r\n"
						+ "FROM\r\n"
						+ "    Doctors_New AS doctors\r\n"
						+ "LEFT JOIN\r\n"
						+ "    DoctorAddresses AS da ON doctors.docid = da.DocID\r\n"
						+ "LEFT JOIN\r\n"
						+ "    DoctorDegrees AS dd ON doctors.docid = dd.DocID\r\n"
						+ "LEFT JOIN\r\n"
						+ "    masterdocdegrees AS mdd ON dd.DegreeID = mdd.DegID\r\n"
						+ "LEFT JOIN\r\n"
						+ "    masteruniversities AS mun ON dd.UnivID = mun.UnivID\r\n"
						+ "LEFT JOIN\r\n"
						+ "    hospital AS h ON doctors.hospital_affliated = h.hospitalid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    specialties AS s ON doctors.primary_spl = s.splid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    states AS st ON doctors.RegWithStateBoardID = st.codeid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    medicinetype AS mt ON doctors.MedicineTypeID = mt.id\r\n"
						+ "LEFT JOIN\r\n"
						+ "    city AS c ON da.City = c.citycode\r\n"
						+ "LEFT JOIN\r\n"
						+ "    states ON da.State = states.codeid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    countries AS co ON da.Country = co.countrycodeid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    city AS uc ON mun.UnivCity = uc.citycode\r\n"
						+ "LEFT JOIN\r\n"
						+ "    states us ON mun.UnivState = us.codeid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    countries AS uco ON mun.UnivCountry = uco.countrycodeid\r\n"
						+ "LEFT JOIN\r\n"
						+ "    masteraddresstype AS mat ON da.AddressTypeID = mat.ID\r\n"
						+ "WHERE\r\n"
						+ "    doctors.docid ="
						+ "" + docid + ";");
		
		// This should return in only 1 doctor so why the List?
		// We should be using query.getSingleResult()
		List<Doctor_New> docsList = (List<Doctor_New>) query.getResultList();
		Doctor_New doc = null;
		Iterator itr = docsList.iterator();
		Constant.log("Executed Query and Got:" + docsList.size() + " doctors back", 1);

		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			{
				doc = new Doctor_New();
				//doc.setDocid((Integer) obj[0]);
				doc.setDocID(obj[0] != null ? (Integer) obj[0] : 0);
				doc.setGender(obj[1] != null ? (Integer) obj[1] : 0);
	//			doc.setEduTraining(obj[2] != null ? (String) obj[2] : "");
				doc.setInsuranceAccept(obj[2] != null && ((Number) obj[2]).intValue() != 0);
				doc.setAwards(obj[3] != null ? (String) obj[3] : "");
				doc.setTelephoneNos(obj[4] != null ? (String) obj[4] : "");
				doc.setOther_spls(obj[5] != null ? (String) obj[5] : "");
				float overall = (float) (obj[6] != null ? (Float) obj[6] : 0.0);
				doc.setOverallRating(overall);
				doc.setPrefix((String) obj[7] != null ? (String) obj[7] : "");
				doc.setFirstName((String) obj[8] != null ? (String) obj[8] : "");
				doc.setMiddleName(obj[9] != null ? (String) obj[9] : "");
				doc.setLastName(obj[10] != null ? (String) obj[10] : "");
				doc.setEmail(obj[11] != null ? (String) obj[11] : "");
				doc.setWaitingTime(obj[12] != null ? (Integer) obj[12] : 0);
				doc.setVerified(obj[13] != null ? (Integer) obj[13] : 0);
				doc.setAbout(obj[14] != null ? (String) obj[14] : "");
				doc.setDocActive(obj[15] != null && ((Number) obj[15]).intValue() != 0);
				doc.setWebsiteUrl(obj[16] != null ? (String) obj[16] : "");
				doc.setFeaturedDoctorDate(obj[17] != null ? (Date) obj[17] : null);
				doc.setImageLocation(obj[18] != null ? (String) obj[18] : "");
				doc.setNationalRegistrationDate(obj[19] != null ? (Date) obj[19] : null);
				doc.setNationalRegistrationNumber(obj[20] != null ? (String) obj[20] : "");
				doc.setCreatedDate((Timestamp) (obj[21] != null ? obj[21] : null));
				doc.setCreatedBy(obj[22] != null ? (Integer) obj[22] : 0);
				doc.setStatus(obj[23] != null ? (Integer) obj[23] : 0);
				doc.setLastUpdatedDate((Timestamp) (obj[24] != null ? obj[24] : null));
				doc.setUpdatedBy(obj[25] != null ? (Integer) obj[25] : 0);
				doc.setHospital_Affiliated(obj[26] != null ? (String) obj[26] : "");
				doc.setPrimary_Spl(obj[27] != null ? (String) obj[27] : "");
				doc.setRegBoardState(obj[28] != null ? (String) obj[28] : "");
				doc.setMedicineType(obj[29] != null ? (String) obj[29] : "");
				doc.setAddress1(obj[30] != null ? (String) obj[30] : "");
				doc.setAddress2(obj[31] != null ? (String) obj[31] : "");
				doc.setCity(obj[32] != null ? (String) obj[32] : "");
				doc.setState(obj[33] != null ? (String) obj[33] : "");
				doc.setCountry(obj[34] != null ? (String) obj[34] : "");
				doc.setAddressType(obj[35] != null ? (String) obj[35] : "");
				doc.setDegDesc(obj[36] != null ? (String) obj[36] : "");
				doc.setYearofGrad(obj[37] != null ? (Integer) obj[37] : 0);
				doc.setUnivName(obj[38] != null ? (String) obj[38] : "");
				doc.setUnivCity(obj[39] != null ? (String) obj[39] : "");
				doc.setUnivState(obj[40] != null ? (String) obj[40] : "");
				doc.setUnivCountry(obj[41] != null ? (String) obj[41] : "");
				
			}
			Constant.log("--Returning from DoctorsDao ", 1);
		}
//		session.getTransaction().commit();   //session.close();
		return doc;
	}

	@Transactional
	public static int saveDoctors( String f_name, String l_name, String email) {
		// creating session factory object
		Session session = HibernateUtil.buildSessionFactory();
		Constant.log("Saving New Doctor with Firstname to DB:" + f_name, 0);
		int docid = 0;
		String prefix="Dr.";
		Doctor_New doctorFound = findDoctorsByEmail(email);
		if (null == doctorFound) {
		try {
			session.beginTransaction();
			Query query = session.createNativeQuery("insert into  Doctors_New ( prefix,docname_first,docname_last,email) values ('" + prefix + "', '"
							+ f_name + "','" + l_name + "','" + email + "');");
	
			 int affectedRows = query.executeUpdate();
			if (affectedRows > 0) {
			// If the query was executed successfully, retrieve the generated docid
			docid = DoctorsDaoImpl_New.findDoctorsByEmail(email).getDocId();
				}
			System.out.println("insert new doctor with email =  " + email + " ");
			session.getTransaction().commit(); // session.close();
			} catch (Exception e) {
				Constant.log(e.getStackTrace().toString(), 3);
				session.getTransaction().commit(); // session.getTransaction().rollback();
			} finally {
//				session.getTransaction().commit();   //session.close();
			}

		} else if (null == doctorFound.getEmail()) {
			System.out.println("Doctors's Email Address not found");
		} else {
			docid = DoctorsDaoImpl_New.findDoctorsByEmail(email).getDocId();
		}
			return docid;
	}

	public static Doctor_New findDoctorsByEmail(String email) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		Constant.log((">>>>>>>>>>>>>>>>>>" + email), 0);
		int docid = 0;

		Doctor_New doctors = null;
		Query query = session.createNativeQuery("select docid,email from Doctors_New where email='" + email.trim() + "'");
		ArrayList<Doctor_New> list = (ArrayList<Doctor_New>) query.getResultList();
		System.out.println(list);
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			doctors = new Doctor_New();
			Constant.log(">>>>>>>>>>>>>>>>>>Doctors Found for Email:" + email, 1);
			Object[] obj = (Object[]) itr.next();
			{
				doctors.setDocID(obj[0] != null ? (Integer) obj[0] : -1);
				doctors.setEmail((String) obj[1]);
	//			doctors.setRowno(obj[2] != null ? (Long) Long.parseLong(""+obj[2]) : -1);

			}
		}
//		session.getTransaction().commit();   //session.close();
		return doctors;
	}
	
	
	
	
}
