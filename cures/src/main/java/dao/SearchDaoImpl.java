package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
//import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import model.Doctor;
import model.Doctors;
import util.Constant;
import util.HibernateUtil;
import util.SolrUtil;

@Component
public class SearchDaoImpl {

	public static void main(String[] args) {
		SearchDaoImpl search = new SearchDaoImpl();
		Doctor docdetail = new Doctor();

//		search.searchByCityPin("jammu");
	}

	public static List<Doctor> searchByCityPin(String cityname) throws IOException {
		SolrClient client = SolrUtil.buildSolrFactory();
		Doctor doc = new Doctor();
		List<Doctor> docarr = new ArrayList<Doctor>();
		// Map<String,List<String>> doctorSolr = new
		// HashMap<String,List<String>>();
	//	System.out.println("Got Solr Client");
		// final Map<String, String> queryParamMap = new HashMap<String,
		// String>();
		// queryParamMap.put("q", "location:"+city);
		// //queryParamMap.put("fl", "id, name");
		// //queryParamMap.put("sort", "id asc");
		// MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		SolrQuery query = new SolrQuery();
		
		// query.set("q", "name:"+docdetails +" or primary_spl:"+ docdetails +"
		// or sub_spls:"+ docdetails);
		query.set("q", Constant.CITYVALUE + ":" + cityname + Constant.OR + Constant.PIN + ":" + cityname);
		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
     		   try {
    		        if (client != null) {
    	            client.close(); // Close the SolrClient instance
   	         }
  	      }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		final SolrDocumentList documents = response.getResults();

		System.out.println("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) 
		{
			String docid = (String) document.getFirstValue(Constant.DOCID);
			if (null!=docid) {
				doc.setDocID(docid);
			}

			// String firstname = (String)
			// document.getFirstValue("docname_first");
			Integer gender = (Integer) document.getFirstValue(Constant.GENDER);
			doc.setGender(gender);
//			String edu_training = (String) document.getFirstValue(Constant.EDU_TRAINING);
//			doc.setEdu_training(edu_training);
			String hospital_affliated = (String) document.getFieldValue(Constant.HOSPITAL_AFFLIATED);
			doc.setHospitalAffiliated(hospital_affliated);
//			Integer insurance_accept = (Integer) document.getFieldValue(Constant.INSURANCE_ACCEPT);
//			doc.setInsurance_accept(insurance_accept);
//			String board_certifcate = (String) document.getFirstValue(Constant.BOARD_CERTIFICATION);
//			doc.setBoard_certifcate(board_certifcate);
//			String membership = (String) document.getFirstValue(Constant.MEMBERSHIP);
//			doc.setMembership(membership);
//			String awards = (String) document.getFirstValue(Constant.AWARDS);
//			doc.setAwards(awards);
//			String availibity_for_appointment = (String) document.getFirstValue(Constant.AVAILIBITY_FOR_APPOINTMENT);
//			doc.setAvailibity_for_appointment(availibity_for_appointment);
//			String doctor_location = (String) document.getFirstValue(Constant.DOCTOR_LOCATION);
//			doc.setDoctor_location(doctor_location);
			String telephone_nos = (String) document.getFieldValue(Constant.TELEPHONE_NOS);
			doc.setTelephoneNos(telephone_nos);
			String primary_spl = (String) document.getFieldValue(Constant.PRIMARY_SPL);
			doc.setPrimarySpl(primary_spl);
//			String other_spls = (String) document.getFirstValue(Constant.OTHER_SPLS);
//			doc.setOther_spls(other_spls);
//			String sub_spls = (String) document.getFieldValue(Constant.SUB_SPLS);
//			doc.setSub_spls(sub_spls);
			String address1 = (String) document.getFieldValue(Constant.ADDRESS1);
			doc.setAddress1(address1);
//			String address2 = (String) document.getFieldValue(Constant.ADDRESS2);
//			doc.setAddress2(address2);
			String city = (String) document.getFieldValue(Constant.CITYVALUE);
			doc.setCity(city);
			String state = (String) document.getFieldValue(Constant.STATE);
			doc.setState(state);
			String country = (String) document.getFieldValue("country");
			doc.setCountry(country);
			String over_allrating = (String) document.getFieldValue(Constant.OVER_ALLRATING);
			doc.setOverAllRating(over_allrating);
//			String create_date = (String) document.getFieldValue(Constant.CREATE_DATE);
//			doc.setCreate_date(create_date);
//			Integer docactive = (Integer) document.getFieldValue(Constant.DOCACTIVE);
//			doc.setDocactive(docactive);
			String prefix = (String) document.getFieldValue(Constant.PREFIX);
			doc.setPrefix(prefix);
			String docname_first = (String) document.getFieldValue(Constant.DOCNAME_FIRST);
			doc.setFirstName(docname_first);
			String docname_middle = (String) document.getFieldValue(Constant.DOCNAME_MIDDLE);
			doc.setMiddleName(docname_middle);
			String docname_last = (String) document.getFieldValue(Constant.DOCNAME_LAST);
			doc.setLastName(docname_last);
			String name = (String) document.getFieldValue(Constant.NAME);
			doc.setFullName(name);
			String email = (String) document.getFieldValue(Constant.EMAIL);
			doc.setEmail(email);
//			String waiting_time = (String) document.getFieldValue(Constant.WAITING_TIME);
//			doc.setWaiting_time(waiting_time);
			String about = (String) document.getFieldValue("about");
			doc.setAbout(about);
			String img_Loc = (String) document.getFirstValue("img_Loc");
			doc.setImgLoc(img_Loc);
			
			/*
			 * String latitude = (String) document.getFieldValue(Constant.LATITUDE);
			 * doc.setLatitude(latitude); String longitude = (String)
			 * document.getFieldValue(Constant.LONGITUDE); doc.setLongitude(longitude);
			 */
	//		System.out.println("doctor" + doc.getPrimarySpl());
			// docarr.add("gender:"+doc.getGender()+",city:"+doc.getCity()+",waiting_time:"+doc.getWaiting_time()+",prefix:"+doc.getPrefix()+",sub_spls:"+doc.getSub_spls()+",docactive:"+docactive+",telephone_nos:"+telephone_nos+",docname_last:"+docname_last+",country:"+country+",over_allrating:"+over_allrating+",
			// doctorid:"+doctorid+", docname_first:"+doc.getFirstName()+",
			// state:"+state+"
			// insurance_accept:"+insurance_accept+",primary_spl:"+primary_spl+",
			// hospital_affliated:"+hospital_affliated+",docname_middle:
			// "+docname_middle+"");
//			docarr.add(new Doctor(doc.getDocID(), doc.getGender(), 
//					doc.getHospitalAffiliated(), doc.getInsurance_accept(), doc.getBoard_certifcate(),
//					doc.getMembership(), doc.getAwards(), doc.getAvailibity_for_appointment(), doc.getDoctor_location(),
//					doc.getTelephoneNos(), doc.getPrimarySpl(), doc.getOther_spls(), doc.getSub_spls(),
//					doc.getAddress1(), doc.getAddress2(), doc.getCity(), doc.getState(), doc.getCountry(),
//					doc.getOverAllRating(), doc.getCreate_date(), doc.getDocactive(), doc.getPrefix(),
//					doc.getFirstName(), doc.getMiddleName, doc.getLastName(), doc.getFullName(),
//					doc.getEmail(), doc.getWaiting_time(), doc.getPincode(), doc.getLatlon(), doc.getRowno()));
			
			
			
			docarr.add(new Doctor(doc.getDocID(), doc.getGender(), 
					doc.getHospitalAffiliated(),
					doc.getTelephoneNos(), doc.getPrimarySpl(), 
					doc.getAddress1(), doc.getCity(), doc.getState(), doc.getCountry(),
					doc.getOverAllRating(), doc.getPrefix(),
					doc.getFirstName(), doc.getMiddleName(), doc.getLastName(), doc.getFullName(),
					doc.getEmail(), doc.getAbout(),doc.getImgLoc(),doc.getMedicineType(),doc.getVideoService(),doc.getDegDesc()));
			// System.out.println("id: " + doctorid + "; for: " + docname_first);

		}
		// System.out.println("SSSSSSSSSSSSSSSSSS" + docarr.size());

		return docarr;
	}

	public static List<Doctor> searchByDocSpl(String docdetails) throws IOException{
		
		SolrClient client = SolrUtil.buildSolrFactory();
		Doctor doc = new Doctor();
		List<Doctor> docarr = new ArrayList<Doctor>();
		// Map<String,List<String>> doctorSolr = new
		// HashMap<String,List<String>>();
	//	System.out.println("Got Solr Client");
		// final Map<String, String> queryParamMap = new HashMap<String,
		// String>();
		// queryParamMap.put("q", "location:"+city);
		// //queryParamMap.put("fl", "id, name");
		// //queryParamMap.put("sort", "id asc");
		// MapSolrParams queryParams = new MapSolrParams(queryParamMap);
//		System.out.println("Lat" + lat);
//		System.out.println("Lon" + lon);
		SolrQuery query = new SolrQuery();
		String[] dname = docdetails.split(" ");
	//	System.out.println("length" + dname.length);
		StringBuilder queryStringBuilder = new StringBuilder();
		if (dname[0].contains("Dr") || dname[0].contains("Dr.")) {
            // Append the field name and the first name
            queryStringBuilder.append("docname_first:").append(dname[1]);

            // Append OR clauses for middle and last names if they exist
            for (int i = 2; i < dname.length; i++) {
                if (!dname[i].trim().isEmpty()) {
                    if (i == dname.length - 1) {
                        // Last name
                        queryStringBuilder.append(" OR ");
                        queryStringBuilder.append("docname_last:").append(dname[i]);
                    } else {
                        // Middle name
                        queryStringBuilder.append(" OR ");
                        queryStringBuilder.append("docname_middle:").append(dname[i]);
                    }
                   
                }
            }
	//		System.out.println(queryStringBuilder.toString());
			 query.add("q", queryStringBuilder.toString());
        } else {
		    // Handle cases where the name doesn't contain "Dr" or "Dr."
		    query.add("q", Constant.NAME + ":" + docdetails + Constant.OR + Constant.PRIMARY_SPL + ":" + docdetails + Constant.OR + Constant.SUB_SPLS + ":" + docdetails);
		}
//		query.add("sort", "geodist() asc");
//		query.add("fq", "{!geofilt sfield=location}");
//		query.add("pt", docloc);
//		query.add("sfield", "docloc1");
//		query.add("d", "50");
		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
 		       try {
	            if (client != null) {
	                client.close(); // Close the SolrClient instance
	            }
	        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		final SolrDocumentList documents = response.getResults();

		System.out.println("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) {
			String docid = (String) document.getFirstValue(Constant.DOCID);
			if (null!=docid) {
				doc.setDocID(docid);
			}
			// String firstname = (String)
			// document.getFirstValue("docname_first");
			Integer gender = (Integer) document.getFirstValue(Constant.GENDER);
			doc.setGender(gender);
//			String edu_training = (String) document.getFirstValue(Constant.EDU_TRAINING);
//			doc.setEdu_training(edu_training);
			String hospital_affliated = (String) document.getFieldValue(Constant.HOSPITAL_AFFLIATED);
			doc.setHospitalAffiliated(hospital_affliated);
//			Integer insurance_accept = (Integer) document.getFieldValue(Constant.INSURANCE_ACCEPT);
//			doc.setInsurance_accept(insurance_accept);
//			String board_certifcate = (String) document.getFirstValue(Constant.BOARD_CERTIFICATION);
//			doc.setBoard_certifcate(board_certifcate);
//			String membership = (String) document.getFirstValue(Constant.MEMBERSHIP);
//			doc.setMembership(membership);
//			String awards = (String) document.getFirstValue(Constant.AWARDS);
//			doc.setAwards(awards);
//			String availibity_for_appointment = (String) document.getFirstValue(Constant.AVAILIBITY_FOR_APPOINTMENT);
//			doc.setAvailibity_for_appointment(availibity_for_appointment);
//			String doctor_location = (String) document.getFirstValue(Constant.DOCTOR_LOCATION);
//			doc.setDoctor_location(doctor_location);
			String telephone_nos = (String) document.getFieldValue(Constant.TELEPHONE_NOS);
			doc.setTelephoneNos(telephone_nos);
			String primary_spl = (String) document.getFieldValue(Constant.PRIMARY_SPL);
			doc.setPrimarySpl(primary_spl);
//			String other_spls = (String) document.getFirstValue(Constant.OTHER_SPLS);
//			doc.setOther_spls(other_spls);
//			String sub_spls = (String) document.getFieldValue(Constant.SUB_SPLS);
//			doc.setSub_spls(sub_spls);
			String address1 = (String) document.getFieldValue(Constant.ADDRESS1);
			doc.setAddress1(address1);
			String address2 = (String) document.getFieldValue(Constant.ADDRESS2);
			doc.setAddress2(address2);
			String city = (String) document.getFieldValue(Constant.CITYVALUE);
			doc.setCity(city);
			String state = (String) document.getFieldValue(Constant.STATE);
			doc.setState(state);
			String country = (String) document.getFieldValue("country");
			doc.setCountry(country);
			String over_allrating = (String) document.getFieldValue(Constant.OVER_ALLRATING);
			doc.setOverAllRating(over_allrating);
//			String create_date = (String) document.getFieldValue(Constant.CREATE_DATE);
//			doc.setCreate_date(create_date);
//			Integer docactive = (Integer) document.getFieldValue(Constant.DOCACTIVE);
//			doc.setDocactive(docactive);
			String prefix = (String) document.getFieldValue(Constant.PREFIX);
			doc.setPrefix(prefix);
			String docname_first = (String) document.getFieldValue(Constant.DOCNAME_FIRST);
			doc.setFirstName(docname_first);
			String docname_middle = (String) document.getFieldValue(Constant.DOCNAME_MIDDLE);
			doc.setMiddleName(docname_middle);
			String docname_last = (String) document.getFieldValue(Constant.DOCNAME_LAST);
			doc.setLastName(docname_last);
			String name = (String) document.getFieldValue(Constant.NAME);
			doc.setFullName(name);
			String email = (String) document.getFieldValue(Constant.EMAIL);
			doc.setEmail(email);
//			String waiting_time = (String) document.getFieldValue(Constant.WAITING_TIME);
//			doc.setWaiting_time(waiting_time);
			
			String img_Loc = (String) document.getFieldValue("img_Loc");
			doc.setImgLoc(img_Loc);
			
			/*
			 * String latitude = (String) document.getFieldValue(Constant.LATITUDE);
			 * doc.setLatitude(latitude); String longitude = (String)
			 * document.getFieldValue(Constant.LONGITUDE); doc.setLongitude(longitude);
			 */

			// System.out.println("doctor" + doc.getFirstName());
			// docarr.add("gender:"+doc.getGender()+",city:"+doc.getCity()+",waiting_time:"+doc.getWaiting_time()+",prefix:"+doc.getPrefix()+",sub_spls:"+doc.getSub_spls()+",docactive:"+docactive+",telephone_nos:"+telephone_nos+",docname_last:"+docname_last+",country:"+country+",over_allrating:"+over_allrating+",
			// doctorid:"+doctorid+", docname_first:"+doc.getFirstName()+",
			// state:"+state+"
			// insurance_accept:"+insurance_accept+",primary_spl:"+primary_spl+",
			// hospital_affliated:"+hospital_affliated+",docname_middle:
			// "+docname_middle+"");
			
			docarr.add(new Doctor(doc.getDocID(), doc.getGender(), 
					doc.getHospitalAffiliated(),
					doc.getTelephoneNos(), doc.getPrimarySpl(), 
					doc.getAddress1(), doc.getCity(), doc.getState(), doc.getCountry(),
					doc.getOverAllRating(), doc.getPrefix(),
					doc.getFirstName(), doc.getMiddleName(), doc.getLastName(), doc.getFullName(),
					doc.getEmail(),   doc.getAbout(),doc.getImgLoc(),doc.getMedicineType(),doc.getVideoService(),doc.getDegDesc()));
			// System.out.println("id: " + doctorid + "; for: " + docname_first);

		}
		// System.out.println("SSSSSSSSSSSSSSSSSS" + docarr.size());

		return docarr;
	}

	public static List<Doctor> searchByBoth(String docdetails, String cityname) throws IOException {
		SolrClient client = SolrUtil.buildSolrFactory();
		Doctor doc = new Doctor();
		List<Doctor> docarr = new ArrayList<Doctor>();
		// Map<String,List<String>> doctorSolr = new
		// HashMap<String,List<String>>();
	//	System.out.println("Got Solr Client for SearchBy Both, Doctor:" + docdetails + ": and city:" + cityname);
		// final Map<String, String> queryParamMap = new HashMap<String,
		// String>();
		// queryParamMap.put("q", "location:"+city);
		// //queryParamMap.put("fl", "id, name");
		// //queryParamMap.put("sort", "id asc");
		// MapSolrParams queryParams = new MapSolrParams(queryParamMap);

		SolrQuery query = new SolrQuery();
		query.set("q",
				Constant.NAME + ":" + docdetails + Constant.OR + Constant.PRIMARY_SPL + ":" + docdetails + Constant.OR
						+ Constant.SUB_SPLS + ":" + docdetails + Constant.AND + Constant.CITYVALUE + ":" + cityname
						+ Constant.OR + Constant.PIN + ":" + cityname);
		// query.set("q", "city:"+cityname +" or pincode:"+ cityname );
		// query.set("q", "name:"+docdetails +" or primary_spl:"+ docdetails +"
		// or sub_spls:"+ docdetails);
		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        try {
	            if (client != null) {
	                client.close(); // Close the SolrClient instance
	            }
	        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		final SolrDocumentList documents = response.getResults();

		System.out.println("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) {
			String docid = (String) document.getFirstValue(Constant.DOCID);
			if (null!=docid) {
				doc.setDocID(docid);
			}
			// String firstname = (String)
			// document.getFirstValue("docname_first");
			Integer gender = (Integer) document.getFirstValue(Constant.GENDER);
			doc.setGender(gender);
//			String edu_training = (String) document.getFirstValue(Constant.EDU_TRAINING);
//			doc.setEdu_training(edu_training);
			String hospital_affliated = (String) document.getFieldValue(Constant.HOSPITAL_AFFLIATED);
			doc.setHospitalAffiliated(hospital_affliated);
//			Integer insurance_accept = (Integer) document.getFieldValue(Constant.INSURANCE_ACCEPT);
//			doc.setInsurance_accept(insurance_accept);
//			String board_certifcate = (String) document.getFirstValue(Constant.BOARD_CERTIFICATION);
//			doc.setBoard_certifcate(board_certifcate);
//			String membership = (String) document.getFirstValue(Constant.MEMBERSHIP);
//			doc.setMembership(membership);
//			String awards = (String) document.getFirstValue(Constant.AWARDS);
//			doc.setAwards(awards);
			
//			String doctor_location = (String) document.getFirstValue(Constant.DOCTOR_LOCATION);
//			doc.setDoctor_location(doctor_location);
			String telephone_nos = (String) document.getFieldValue(Constant.TELEPHONE_NOS);
			doc.setTelephoneNos(telephone_nos);
			String primary_spl = (String) document.getFieldValue(Constant.PRIMARY_SPL);
			doc.setPrimarySpl(primary_spl);
//			String other_spls = (String) document.getFirstValue(Constant.OTHER_SPLS);
//			doc.setOther_spls(other_spls);
//			String sub_spls = (String) document.getFieldValue(Constant.SUB_SPLS);
//			doc.setSub_spls(sub_spls);
			String address1 = (String) document.getFieldValue(Constant.ADDRESS1);
			doc.setAddress1(address1);
//			String address2 = (String) document.getFieldValue(Constant.ADDRESS2);
//			doc.setAddress2(address2);
			String city = (String) document.getFieldValue(Constant.CITYVALUE);
			doc.setCity(city);
			String state = (String) document.getFieldValue(Constant.STATE);
			doc.setState(state);
			String country = (String) document.getFieldValue("country");
			doc.setCountry(country);
			String over_allrating = (String) document.getFieldValue(Constant.OVER_ALLRATING);
			doc.setOverAllRating(over_allrating);
//			String create_date = (String) document.getFieldValue(Constant.CREATE_DATE);
//			doc.setCreate_date(create_date);
//			Integer docactive = (Integer) document.getFieldValue(Constant.DOCACTIVE);
//			doc.setDocactive(docactive);
			String prefix = (String) document.getFieldValue(Constant.PREFIX);
			doc.setPrefix(prefix);
			String docname_first = (String) document.getFieldValue(Constant.DOCNAME_FIRST);
			doc.setFirstName(docname_first);
			String docname_middle = (String) document.getFieldValue(Constant.DOCNAME_MIDDLE);
			doc.setMiddleName(docname_middle);
			String docname_last = (String) document.getFieldValue(Constant.DOCNAME_LAST);
			doc.setLastName(docname_last);
			String name = (String) document.getFieldValue(Constant.NAME);
			doc.setFullName(name);
			String email = (String) document.getFieldValue(Constant.EMAIL);
			doc.setEmail(email);
//			String waiting_time = (String) document.getFieldValue(Constant.WAITING_TIME);
//			doc.setWaiting_time(waiting_time);
			
			String about = (String) document.getFieldValue("about");
			doc.setAbout(about);
			String img_Loc = (String) document.getFieldValue("img_Loc");
			doc.setImgLoc(img_Loc);
			/*
			 * String latitude = (String) document.getFieldValue(Constant.LATITUDE);
			 * doc.setLatitude(latitude); String longitude = (String)
			 * document.getFieldValue(Constant.LONGITUDE); doc.setLongitude(longitude);
			 */

			// System.out.println("doctor" + doc.getFirstName());
			// docarr.add("gender:"+doc.getGender()+",city:"+doc.getCity()+",waiting_time:"+doc.getWaiting_time()+",prefix:"+doc.getPrefix()+",sub_spls:"+doc.getSub_spls()+",docactive:"+docactive+",telephone_nos:"+telephone_nos+",docname_last:"+docname_last+",country:"+country+",over_allrating:"+over_allrating+",
			// doctorid:"+doctorid+", docname_first:"+doc.getFirstName()+",
			// state:"+state+"
			// insurance_accept:"+insurance_accept+",primary_spl:"+primary_spl+",
			// hospital_affliated:"+hospital_affliated+",docname_middle:
			// "+docname_middle+"");
			
			docarr.add(new Doctor(doc.getDocID(), doc.getGender(), 
					doc.getHospitalAffiliated(),
					doc.getTelephoneNos(), doc.getPrimarySpl(), 
					doc.getAddress1(), doc.getCity(), doc.getState(), doc.getCountry(),
					doc.getOverAllRating(), doc.getPrefix(),
					doc.getFirstName(), doc.getMiddleName(), doc.getLastName(), doc.getFullName(),
					doc.getEmail(), doc.getAbout(),doc.getImgLoc(),doc.getMedicineType(),doc.getVideoService(),doc.getDegDesc()));
			// System.out.println("id: " + doctorid + "; for: " + docname_first);

		}
		// System.out.println("SSSSSSSSSSSSSSSSSS" + docarr.size());

		return docarr;
	}
	public static List<Doctor> featuredDoctors(String featureddocdetails) throws IOException {
		SolrClient client = SolrUtil.buildSolrFactory();
		Doctor doc = new Doctor();
		List<Doctor> docarr = new ArrayList<Doctor>();
		// Map<String,List<String>> doctorSolr = new
		// HashMap<String,List<String>>();
	//	System.out.println("Got Solr Client for featuredDoctors For Doctor:" + featureddocdetails);
		// final Map<String, String> queryParamMap = new HashMap<String,
		// String>();
		// queryParamMap.put("q", "location:"+city);
		// //queryParamMap.put("fl", "id, name");
		// //queryParamMap.put("sort", "id asc");
		// MapSolrParams queryParams = new MapSolrParams(queryParamMap);
		
		String[] featuredDocArr = featureddocdetails.split(",");
		String queryStr = "";
		for (String featuredDoc : featuredDocArr) {
			queryStr = queryStr + Constant.DOCID + ":" + featuredDoc + Constant.OR;
		}
		
//		Constant.log(queryStr,0);
		
		SolrQuery query = new SolrQuery();
		query.setRows(Integer.MAX_VALUE);
		query.set("q",queryStr);
//		query.set("sort", "docid desc");
		QueryResponse response = null;
		try {
			response = client.query(query);
//			System.out.println("Raw Solr Response: " + response.getResponse());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
   	     try {
	            if (client != null) {
	                client.close(); // Close the SolrClient instance
	            }
	        }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		final SolrDocumentList documents = response.getResults();
		
		System.out.println("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) {
			String docid = (String) document.getFirstValue(Constant.DOCID);
				if (null!=docid) {
				doc.setDocID(docid);
		//		System.out.println("DocID from Solr:" +docid);
			}

	//		Object rowno_ = document.getFieldValue(Constant.ROWNO);
			// String firstname = (String)
			// document.getFirstValue("docname_first");
			Integer gender = (Integer) document.getFirstValue(Constant.GENDER);
			doc.setGender(gender);
//			String edu_training = (String) document.getFirstValue(Constant.EDU_TRAINING);
//			doc.setEdu_training(edu_training);
			String hospital_affliated = (String) document.getFieldValue(Constant.HOSPITAL_AFFLIATED);
			doc.setHospitalAffiliated(hospital_affliated);
//			Integer insurance_accept = (Integer) document.getFieldValue(Constant.INSURANCE_ACCEPT);
//			doc.setInsurance_accept(insurance_accept);
//			String board_certifcate = (String) document.getFirstValue(Constant.BOARD_CERTIFICATION);
//			doc.setBoard_certifcate(board_certifcate);
//			String membership = (String) document.getFirstValue(Constant.MEMBERSHIP);
//			doc.setMembership(membership);
//			String awards = (String) document.getFirstValue(Constant.AWARDS);
//			doc.setAwards(awards);
//			String doctor_location = (String) document.getFirstValue(Constant.DOCTOR_LOCATION);
//			doc.setDoctor_location(doctor_location);
			String telephone_nos = (String) document.getFieldValue(Constant.TELEPHONE_NOS);
			doc.setTelephoneNos(telephone_nos);
			String primary_spl = (String) document.getFieldValue(Constant.PRIMARY_SPL);
			doc.setPrimarySpl(primary_spl);
//			String other_spls = (String) document.getFirstValue(Constant.OTHER_SPLS);
//			doc.setOther_spls(other_spls);
//			String sub_spls = (String) document.getFieldValue(Constant.SUB_SPLS);
//			doc.setSub_spls(sub_spls);
			String address1 = (String) document.getFieldValue(Constant.ADDRESS1);
			doc.setAddress1(address1);
//			String address2 = (String) document.getFieldValue(Constant.ADDRESS2);
//			doc.setAddress2(address2);
			String city = (String) document.getFieldValue(Constant.CITYVALUE);
			doc.setCity(city);
			String state = (String) document.getFieldValue(Constant.STATE);
			doc.setState(state);
			String country = (String) document.getFieldValue("country");
	//		System.out.println("country"+country);
			doc.setCountry(country);
			String over_allrating = (String) document.getFieldValue(Constant.OVER_ALLRATING);
			doc.setOverAllRating(over_allrating);
//			String create_date = (String) document.getFieldValue(Constant.CREATE_DATE);
//			doc.setCreate_date(create_date);
//			Integer docactive = (Integer) document.getFieldValue(Constant.DOCACTIVE);
//			doc.setDocactive(docactive);
			String prefix = (String) document.getFieldValue(Constant.PREFIX);
			doc.setPrefix(prefix);
			String docname_first = (String) document.getFieldValue(Constant.DOCNAME_FIRST);
			doc.setFirstName(docname_first);
			String docname_middle = (String) document.getFieldValue(Constant.DOCNAME_MIDDLE);
			doc.setMiddleName(docname_middle);
			String docname_last = (String) document.getFieldValue(Constant.DOCNAME_LAST);
			doc.setLastName(docname_last);
			String name = (String) document.getFieldValue(Constant.NAME);
			doc.setFullName(name);
			String email = (String) document.getFieldValue(Constant.EMAIL);
			doc.setEmail(email);
//			String waiting_time = (String) document.getFieldValue(Constant.WAITING_TIME);
//			doc.setWaiting_time(waiting_time);
			
			String about = (String) document.getFieldValue("about");
			doc.setAbout(about);
			String img_Loc = (String) document.getFieldValue("img_Loc");
	//		System.out.println("img_Loc"+img_Loc);
			doc.setImgLoc(img_Loc);
			String MedicineType = (String) document.getFieldValue("medicine_type");
			 doc.setMedicineType(MedicineType);
			String VideoService = (String) document.getFieldValue("videoService");
			 doc.setVideoService(VideoService);
			 String DegDesc = (String) document.getFieldValue("degDesc");
			 doc.setDegDesc(DegDesc);
			/*
			 * String latitude = (String) document.getFieldValue(Constant.LATITUDE);
			 * doc.setLatitude(latitude); String longitude = (String)
			 * document.getFieldValue(Constant.LONGITUDE); doc.setLongitude(longitude);
			 */
			
			// System.out.println("doctor" + doc.getFirstName());
			// docarr.add("gender:"+doc.getGender()+",city:"+doc.getCity()+",waiting_time:"+doc.getWaiting_time()+",prefix:"+doc.getPrefix()+",sub_spls:"+doc.getSub_spls()+",docactive:"+docactive+",telephone_nos:"+telephone_nos+",docname_last:"+docname_last+",country:"+country+",over_allrating:"+over_allrating+",
			// doctorid:"+doctorid+", docname_first:"+doc.getFirstName()+",
			// state:"+state+"
			// insurance_accept:"+insurance_accept+",primary_spl:"+primary_spl+",
			// hospital_affliated:"+hospital_affliated+",docname_middle:
			// "+docname_middle+"");
			
			docarr.add(new Doctor(doc.getDocID(), doc.getGender(), 
					doc.getHospitalAffiliated(),
					doc.getTelephoneNos(), doc.getPrimarySpl(), 
					doc.getAddress1(), doc.getCity(), doc.getState(), doc.getCountry(),
					doc.getOverAllRating(), doc.getPrefix(),
					doc.getFirstName(), doc.getMiddleName(), doc.getLastName(), doc.getFullName(),
					doc.getEmail(), doc.getAbout(),doc.getImgLoc(),doc.getMedicineType(),doc.getVideoService(),doc.getDegDesc()));
			// System.out.println("id: " + doctorid + "; for: " + docname_first);
			
		}
		// System.out.println("SSSSSSSSSSSSSSSSSS" + docarr.size());
		
		return docarr;
	}

	public static List findRegionsNameForDiseaseId(int dc_id) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		//Session session = factory;

		// creating transaction object
//		 session.beginTransaction();

		Query query = session.createNativeQuery(
				"select c.countryname, a.country_id, a.type, a.disease_condition_id, a.article_id, a.title FROM article a\r\n"
						+ " left join countries c on a.country_id = c.countrycodeid " //or a.country_id is null \r\n"
						+ " where a.disease_condition_id = " + dc_id + "\r\n;");// + " and FIND_IN_SET (2, a.type) > 0 ;");

		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			String countryname = (String) objects[0];
			Integer country_id = (Integer) objects[1];
			String type = (String) objects[2];
			Integer disease_condition_id = (Integer) objects[3];
			Integer article_id = (Integer) objects[4];
			String title = (String) objects[5];
			hm.put("countryname", countryname);
			hm.put("country_id", country_id);
			hm.put("type", type);
			hm.put("disease_condition_id", disease_condition_id);
			hm.put("article_id", article_id);
			hm.put("title", title);
			hmFinal.add(hm);
		}
//		session.getTransaction().commit();   //session.close();
		return hmFinal;

	}
}
