package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import java.time.LocalDateTime;

import model.RotationTracker;
import util.Constant;
import util.HibernateUtil;

public class DocRotationDaoImpl {

	public static List<Integer> getRotatedDocIds() {
	Session session = HibernateUtil.buildSessionFactory();

        try {
            // Fetch last rotation index
            int lastIndex = getLastIndex(session);

         // Corrected native query
            NativeQuery<Integer> query = session.createNativeQuery(
                "SELECT doctors.docid " +
                "FROM Doctors_New AS doctors " +
                "JOIN ( " +
                "    SELECT r.DocID, sr.ServiceID, sr.fee " +
                "    FROM registration r " +
                "    JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "    WHERE sr.ServiceID = 2 AND sr.EndDate >= CURRENT_DATE " +
                ") AS sr ON doctors.docid = sr.DocID " +
                "WHERE (doctors.docid <= 63 OR doctors.docid >= 14487) " +
               "ORDER BY doctors.docid DESC Limit 3 offset " + lastIndex)
                .addScalar("docid", org.hibernate.type.IntegerType.INSTANCE); // Map docid as Integer

            List<Integer> docIds = query.list();
		 System.out.println("docIds"+docIds);
            if (!docIds.isEmpty()) {
                // Update the index for the next rotation
                int totalCount = docIds.size();
                int newOffset = (lastIndex + 3) % totalCount;
		    System.out.println("newOffset"+newOffset);
                updateLastIndex(session, newOffset);
            }
            return docIds;

        } catch (Exception e) {
            
            e.printStackTrace();
            return null;
        } 
    }

	// Retrieve the last rotation index
	private static int getLastIndex(Session session) {
	    List<Integer> result = session.createQuery("SELECT r.lastIndex FROM RotationTracker r WHERE r.id = 1", Integer.class)
	                                  .list();
	    
	    if (!result.isEmpty()) {
	        return result.get(0);
	    } else {
	        // If no record exists, initialize it
	        RotationTracker rotationTracker = new RotationTracker();
	        rotationTracker.setId(1);
	        rotationTracker.setLastIndex(0);
	        rotationTracker.setUpdatedAt(LocalDateTime.now());
	        session.save(rotationTracker);
	        
	        return 0;
	    }
	}

    
 // Update the last rotation index
	private static void updateLastIndex(Session session, int newOffset) {
	    session.createQuery("UPDATE RotationTracker r SET r.lastIndex = :newIndex, r.updatedAt = :updatedAt WHERE r.id = 1")
	           .setParameter("newIndex", newOffset)
	           .setParameter("updatedAt", LocalDateTime.now()) // Set updated timestamp
	           .executeUpdate();
	}

    
    public static  List<HashMap<String, Object>> getDoctorsList() {
        Session session = HibernateUtil.buildSessionFactory();
        int offset = getLastIndex(session);
		/*	        Query query1 = session.createNativeQuery("SELECT d.docid, d.prefix, d.docname_first, d.docname_middle, d.docname_last,d.img_Loc " +
                "mt.name AS MedicineTypeName, h.hospital_affliated " +
                "FROM Doctors_New d " +
                " LEFT JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID " +
                "JOIN registration r ON d.docid = r.DocID " +
                
                "LEFT JOIN hospital AS h ON d.hospital_affliated = h.hospitalid " +
                "JOIN medicinetype AS mt ON d.MedicineTypeID = mt.id " +
                "WHERE sr.ServiceID=2 and EndDate>=current_date();");
*/
        Query query1 = session.createNativeQuery("SELECT\r\n"
        		+ "    doctors.docid, doctors.gender, doctors.insurance_accept,\r\n"
        		+ "    doctors.awards, doctors.telephone_nos, doctors.other_spls, doctors.over_allrating,\r\n"
        		+ "    doctors.prefix, doctors.docname_first, doctors.docname_middle, doctors.docname_last,\r\n"
        		+ "    doctors.email, doctors.waiting_time, doctors.verified, doctors.about, doctors.docactive,\r\n"
        		+ "    doctors.website_url, doctors.featured_doctor_date, doctors.img_loc, doctors.Natl_Reg_Date,\r\n"
        		+ "    doctors.NatlRegNo, doctors.CreatedDate, doctors.CreatedBy, doctors.Status,\r\n"
        		+ "    doctors.LastUpdatedDate, doctors.UpdatedBy, h.hospital_affliated, s.spl_name,\r\n"
        		+ "    st.statename AS state_name, mt.name AS MedicineTypeName,\r\n"
        		+ "    da.Address1, da.Address2, c.cityname, address_states.statename AS address_state,\r\n"
        		+ "    co.countryname AS address_country, mat.AddressType,\r\n"
        		+ "    mdd.DegDesc, dd.YearOfGrad, mun.UnivName,\r\n"
        		+ "    uc.cityname AS univ_city, us.statename AS univ_state, uco.countryname AS univ_country,\r\n"
        		+ "    mt.id, address_states.codeid, uc.citycode, co.countrycodeid,\r\n"
        		+ "    mdd.DegID, s.splid, h.hospitalid, AVG(dr.ratingVal) AS ratingValAVG, sr.fee,\r\n"
        		+ "       CASE WHEN sr.ServiceID = 2 THEN 1 ELSE 0 END AS videoService\r\n"
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
        		+ "JOIN\r\n"
        		+ "    medicinetype AS mt ON doctors.MedicineTypeID = mt.id\r\n"
        		+ "LEFT JOIN\r\n"
        		+ "    city AS c ON da.City = c.citycode\r\n"
        		+ "LEFT JOIN\r\n"
        		+ "    states AS address_states ON da.State = address_states.codeid\r\n"
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
        		+ "LEFT JOIN (\r\n"
        		+ "    SELECT r.DocID, sr.ServiceID, sr.fee\r\n"
        		+ "    FROM registration r\r\n"
        		+ "    JOIN ServiceContractDetails sr ON r.registration_id = sr.UserID\r\n"
        		+ "    WHERE sr.ServiceID = 2 AND EndDate >= CURRENT_DATE\r\n"
        		+ ") AS sr ON doctors.docid = sr.DocID\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    doctorsrating AS dr ON dr.target_id = doctors.docid AND dr.target_type_id = 1\r\n"
        		+ "WHERE doctors.docid <= 63 OR doctors.docid >= 14487\r\n"
        		+ "GROUP BY doctors.docid, doctors.gender, doctors.insurance_accept, doctors.awards, \r\n"
        		+ "    doctors.telephone_nos, doctors.other_spls, doctors.over_allrating, doctors.prefix, \r\n"
        		+ "    doctors.docname_first, doctors.docname_middle, doctors.docname_last, doctors.email, \r\n"
        		+ "    doctors.waiting_time, doctors.verified, doctors.about, doctors.docactive, doctors.website_url, \r\n"
        		+ "    doctors.featured_doctor_date, doctors.img_loc, doctors.Natl_Reg_Date, doctors.NatlRegNo, \r\n"
        		+ "    doctors.CreatedDate, doctors.CreatedBy, doctors.Status, doctors.LastUpdatedDate, doctors.UpdatedBy, \r\n"
        		+ "    h.hospital_affliated, s.spl_name, st.statename, mt.name, da.Address1, da.Address2, c.cityname, \r\n"
        		+ "    address_states.statename, co.countryname, mat.AddressType, mdd.DegDesc, dd.YearOfGrad, mun.UnivName, \r\n"
        		+ "    uc.cityname, us.statename, uco.countryname, mt.id, address_states.codeid, uc.citycode, co.countrycodeid, \r\n"
        		+ "    mdd.DegID, s.splid, h.hospitalid, sr.fee,sr.ServiceID \r\n"
        		+ "ORDER BY \r\n"
        		+ "    CASE \r\n"
        		+ "        WHEN sr.ServiceID = 2 THEN 0\r\n"
        		+ "        ELSE 1\r\n"
        		+ "    END,\r\n"
        		+ "    doctors.docid DESC Limit 3 OFFSET\r\n " + offset 
				  + ";");
        List<HashMap<String, Object>> doctorList = new ArrayList<>();

        List<Object[]> resultList = query1.getResultList();
        Constant.log("Executed Query and Got: " + resultList.size() + " Doctor Lists back", 1);

     // Iterate through results and map each row to a HashMap
        for (Object[] row : resultList) {
            HashMap<String, Object> doctor = new HashMap<>();
            doctor.put("docID", row[0]);
            doctor.put("gender", row[1]);
            doctor.put("insuranceAccept", row[2]);
            doctor.put("awards", row[3]);
            doctor.put("telephoneNos", row[4]);
            doctor.put("otherSpecializations", row[5]);
            doctor.put("overallRating", row[6]);
            doctor.put("prefix", row[7]);
            doctor.put("firstName", row[8]);
            doctor.put("middleName", row[9]);
            doctor.put("lastName", row[10]);
            doctor.put("email", row[11]);
            doctor.put("waitingTime", row[12]);
            doctor.put("verified", row[13]);
            doctor.put("about", row[14]);
            doctor.put("docActive", row[15]);
            doctor.put("websiteUrl", row[16]);
            doctor.put("featuredDoctorDate", row[17]);
            doctor.put("imgLoc", row[18]);
            doctor.put("nationalRegDate", row[19]);
            doctor.put("nationalRegNo", row[20]);
            doctor.put("createdDate", row[21]);
            doctor.put("createdBy", row[22]);
            doctor.put("status", row[23]);
            doctor.put("lastUpdatedDate", row[24]);
            doctor.put("updatedBy", row[25]);
            doctor.put("hospitalAffiliated", row[26]);
            doctor.put("specialtyName", row[27]);
            doctor.put("stateName", row[28]);
            doctor.put("medicineTypeName", row[29]);
            doctor.put("address1", row[30]);
            doctor.put("address2", row[31]);
            doctor.put("cityName", row[32]);
            doctor.put("addressState", row[33]);
            doctor.put("addressCountry", row[34]);
            doctor.put("addressType", row[35]);
            doctor.put("degreeDescription", row[36]);
            doctor.put("yearOfGraduation", row[37]);
            doctor.put("universityName", row[38]);
            doctor.put("universityCity", row[39]);
            doctor.put("universityState", row[40]);
            doctor.put("universityCountry", row[41]);
            doctor.put("medicineTypeID", row[42]);
            doctor.put("addressStateID", row[43]);
            doctor.put("univCityCode", row[44]);
            doctor.put("addressCountryID", row[45]);
            doctor.put("degreeID", row[46]);
            doctor.put("specialtyID", row[47]);
            doctor.put("hospitalID", row[48]);
            doctor.put("ratingValueAverage", row[49]);
            doctor.put("fee", row[50]);
            doctor.put("videoService", row[51] != null ? (Integer) row[51] : 0);
            doctorList.add(doctor);	        
            }
        return doctorList;
    }

     
}
