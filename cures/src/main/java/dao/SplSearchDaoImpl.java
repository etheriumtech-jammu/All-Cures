package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SpatialParams;

import model.Doctor;
import util.Constant;
import util.SolrUtil;

public class SplSearchDaoImpl {
	public static List<Doctor> SplSearchLatLon(String lat, String lon  ) {
		SolrClient client = SolrUtil.buildSolrFactory();
		Doctor doc = new Doctor();
		List<Doctor> docarr = new ArrayList<Doctor>();
		// Map<String,List<String>> doctorSolr = new
		// HashMap<String,List<String>>();
		System.out.println("Got Solr Client");
		ModifiableSolrParams params;
		
		// final Map<String, String> queryParamMap = new HashMap<String,
		// String>();
		// queryParamMap.put("q", "location:"+city);
		// //queryParamMap.put("fl", "id, name");
		// //queryParamMap.put("sort", "id asc");
		// MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        String latlonf =lat+","+lon;
		SolrQuery query = new SolrQuery();
		// query.set("q", "name:"+docdetails +" or primary_spl:"+ docdetails +"
		// or sub_spls:"+ docdetails);
		query.add("pt",   latlonf);
		query.add("sField","docloc");
		query.add("d","50");
		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final SolrDocumentList documents = response.getResults();

		System.out.println("Found " + documents.getNumFound() + " documents");
		for (SolrDocument document : documents) {
			Integer doctorid = (Integer) document.getFirstValue(Constant.DOCID);
			doc.setDoctorid(doctorid.toString());

			// String firstname = (String)
			// document.getFirstValue("docname_first");
			Integer gender = (Integer) document.getFirstValue(Constant.GENDER);
			doc.setGender(gender);
			String edu_training = (String) document.getFirstValue(Constant.EDU_TRAINING);
			doc.setEdu_training(edu_training);
			String hospital_affliated = (String) document.getFieldValue(Constant.HOSPITAL_AFFLIATED);
			doc.setHospital_affliated(hospital_affliated);
			Integer insurance_accept = (Integer) document.getFieldValue(Constant.INSURANCE_ACCEPT);
			doc.setInsurance_accept(insurance_accept);
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
			doc.setTelephone_nos(telephone_nos);
			String primary_spl = (String) document.getFieldValue(Constant.PRIMARY_SPL);
			doc.setPrimary_spl(primary_spl);
//			String other_spls = (String) document.getFirstValue(Constant.OTHER_SPLS);
//			doc.setOther_spls(other_spls);
			String sub_spls = (String) document.getFieldValue(Constant.SUB_SPLS);
			doc.setSub_spls(sub_spls);
			String address1 = (String) document.getFieldValue(Constant.ADDRESS1);
			doc.setAddress1(address1);
//			String address2 = (String) document.getFieldValue(Constant.ADDRESS2);
//			doc.setAddress2(address2);
			String city = (String) document.getFieldValue(Constant.CITYVALUE);
			doc.setCity(city);
			String state = (String) document.getFieldValue(Constant.STATE);
			doc.setState(state);
			String country_code = (String) document.getFieldValue(Constant.COUNTRY_CODE);
			doc.setCountry_code(country_code);
			String over_allrating = (String) document.getFieldValue(Constant.OVER_ALLRATING);
			doc.setOver_allrating(over_allrating);
//			String create_date = (String) document.getFieldValue(Constant.CREATE_DATE);
//			doc.setCreate_date(create_date);
//			Integer docactive = (Integer) document.getFieldValue(Constant.DOCACTIVE);
//			doc.setDocactive(docactive);
			String prefix = (String) document.getFieldValue(Constant.PREFIX);
			doc.setPrefix(prefix);
			String docname_first = (String) document.getFieldValue(Constant.DOCNAME_FIRST);
			doc.setDocname_first(docname_first);
			String docname_middle = (String) document.getFieldValue(Constant.DOCNAME_MIDDLE);
			doc.setDocname_middle(docname_middle);
			String docname_last = (String) document.getFieldValue(Constant.DOCNAME_LAST);
			doc.setDocname_last(docname_last);
			String name = (String) document.getFieldValue(Constant.NAME);
			doc.setName(name);
			String email = (String) document.getFieldValue(Constant.EMAIL);
			doc.setEmail(email);
//			String waiting_time = (String) document.getFieldValue(Constant.WAITING_TIME);
//			doc.setWaiting_time(waiting_time);
			String pincode = (String) document.getFieldValue(Constant.PIN);
			doc.setPincode(pincode);
			String latlon= (String) document.getFieldValue(Constant.LATITUDELONGITUDE);
			doc.setLat(latlon);
			String rowno= (String) document.getFieldValue(Constant.ROWNO);
			doc.setRowno(rowno);
			
			
			System.out.println("doctor" + doc.getPrimary_spl());
			// docarr.add("gender:"+doc.getGender()+",city:"+doc.getCity()+",waiting_time:"+doc.getWaiting_time()+",prefix:"+doc.getPrefix()+",sub_spls:"+doc.getSub_spls()+",docactive:"+docactive+",telephone_nos:"+telephone_nos+",docname_last:"+docname_last+",country_code:"+country_code+",over_allrating:"+over_allrating+",
			// doctorid:"+doctorid+", docname_first:"+doc.getDocname_first()+",
			// state:"+state+"
			// insurance_accept:"+insurance_accept+",primary_spl:"+primary_spl+",
			// hospital_affliated:"+hospital_affliated+",docname_middle:
			// "+docname_middle+"");
			docarr.add(new Doctor(doc.getDoctorid(), doc.getGender(), doc.getEdu_training(),
					doc.getHospital_affliated(),
					doc.getTelephone_nos(), doc.getPrimary_spl(), doc.getSub_spls(),
					doc.getAddress1(), doc.getCity(), doc.getState(), doc.getCountry_code(),
					doc.getOver_allrating(), doc.getPrefix(),
					doc.getDocname_first(), doc.getDocname_middle(), doc.getDocname_last(), doc.getName(),
					doc.getEmail(), doc.getPincode(),doc.getLatlon(), doc.getRowno()));
			//System.out.println("id: " + doctorid + "; for: " + docname_first);

		}
		//System.out.println("SSSSSSSSSSSSSSSSSS" + docarr.size());
		

		return docarr;
	}


}
