package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;

import javax.persistence.Table;

import org.json.JSONObject;

import util.Constant;

@Entity
@Table(name = Constant.DOCTORS)
public class Doctors implements Serializable {

	@Id
	@Column
//@GeneratedValue(strategy = GenerationType.AUTO)
	private int docid = -1;
	@Column
	private int gender = 0;
	@Column
	private String edu_training;
	@Column
	private boolean insurance_accept = false;

	@ManyToOne(targetEntity = Hospital.class)
	private String hospital_affliated;
	@Column
	private String board_certifcate;
	@Column
	private String membership;
	@Column
	private String awards;
	@Column
	private int waiting_time = -1;
	@Column
	private Date availibity_for_appointment;
	@Column
	private String doctor_location;
	@Column
	private String telephone_nos;

	@ManyToOne(targetEntity = Specialties.class)
	private String primary_spl;
	@Column
	private String other_spls;
	@Column
	private int sub_spls = 0;
	@Column
	private String address1;
	@Column
	private String address2;
	@ManyToOne(targetEntity = City.class)
	private String cityname;
	@ManyToOne(targetEntity = States.class)
	private String statename;
	@ManyToOne(targetEntity = States.class)
	private String country_code;
	@Column
	private double over_allrating = 0.0;
	@Column
	private Date create_date;
	@Column
	private int docactive = -1;
	@Column
	private String prefix;
	@Column
	private String docname_first;
	@Column
	private String docname_middle;
	@Column
	private String docname_last;
	@Column
	private String email;

	@ManyToOne
	@JoinColumn(name = Constant.HOSPITALID)
	private Hospital hospital;

	@ManyToOne
	@JoinColumn(name = Constant.SPLID)
	private Specialties specialties;

	@ManyToOne
	@JoinColumn(name = Constant.CITYCODE)
	private City city;

	@ManyToOne
	@JoinColumn(name = Constant.CODEID)
	private States states;

	@ManyToOne
	@JoinColumn(name = Constant.COUNTRYCODEID)
	private countries countries;

	private Integer primary_spl_code;
	private Integer other_spls_code;
	private Integer sub_spls_code;
	private String about;
	private Integer city_code;
	private Integer state_code;
	private Integer countries_code;
	private Integer hospital_affliated_code;
	private Long rowno;
	
	private Integer subscription;

	public Integer getSubscription() {
		return subscription;
	}

	public void setSubscription(Integer subscription) {
		this.subscription = subscription;
	}

	private String website_url;

	public Doctors() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Doctors(String docJsonStr) {
		/*
		 * if(docsSplit != null){ String [] docProfile =
		 * docsSplit.split(Constant.DefaultStringSplitter); JSONObject docJson = new
		 * JSONObject(); String key, value; for(int i=0; i < docProfile.length - 1;
		 * i++){ String []keyVal; keyVal = docProfile[i].split(":");
		 * Constant.log(docProfile[i], 1); key = keyVal[0]; Constant.log(key, 1); value
		 * = keyVal[1]; Constant.log(value, 1); docJson.append(key, value); }
		 */
		/*
		 * //JSON based implementation is better
		 * 
		 * //Json of the Doctor of the format: docid:1|prefix:Mr.| //Need to change the
		 * Seperator to "|"; Maybe do a replaceAll of the DefaultStringSplitter with
		 * (DefaultJsonSplitter) "," here //or set the Json Objects Seperator to "|"
		 * 
		 * JSONParser parser = new JSONParser();
		 * 
		 * docJson = (JSONObject)docJson.stringToValue(docsSplit);
		 */
		Constant.log("Constructing Doctor from JSON String:" + docJsonStr, 1);
		JSONObject docJson = new JSONObject(docJsonStr);
		Constant.log("Converted String to JSON Object", 1);
		this.setDocid((Integer) docJson.get(Constant.DOCID));
		Constant.log("Got Id from JSON Object" + docJson.get(Constant.DOCID), 1);
		if (docJson.get(Constant.GENDER) != null && !JSONObject.NULL.equals(Constant.GENDER)) {
			try {
				this.setGender((Integer) docJson.get(Constant.GENDER));
			} catch (Exception e) {
				e.printStackTrace();
				this.setGender(new Integer(-1));
			}
		}
		if (docJson.get(Constant.EDU_TRAINING) != null && !JSONObject.NULL.equals(docJson.get(Constant.EDU_TRAINING)))
			// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
			this.setEdu_training((String) docJson.get(Constant.EDU_TRAINING));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.INSURANCE_ACCEPT) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.INSURANCE_ACCEPT)))
			this.setInsurance_accept((Boolean) docJson.get(Constant.INSURANCE_ACCEPT));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.HOSPITAL_AFFLIATED) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.HOSPITAL_AFFLIATED)))
			this.setHospital_affliated((String) docJson.get(Constant.HOSPITAL_AFFLIATED));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.BOARD_CERTIFICATION) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.BOARD_CERTIFICATION)))
			this.setBoard_certifcate((String) docJson.get(Constant.BOARD_CERTIFICATION));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.MEMBERSHIP) != null && !JSONObject.NULL.equals(docJson.get(Constant.MEMBERSHIP)))
			this.setMembership((String) docJson.get(Constant.MEMBERSHIP));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.AWARDS) != null && !JSONObject.NULL.equals(docJson.get(Constant.AWARDS)))
			this.setAwards((String) docJson.get(Constant.AWARDS));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.AVAILIBITY_FOR_APPOINTMENT) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.AVAILIBITY_FOR_APPOINTMENT))) {
			try {
				this.setAvailibity_for_appointment((Date) docJson.get(Constant.AVAILIBITY_FOR_APPOINTMENT));
			} catch (Exception e) {
				e.printStackTrace();
				this.setAvailibity_for_appointment((new java.util.Date(System.currentTimeMillis())));
				Constant.log("Error: Setting Avl. For Appt to Default Date (Now)", 3);
			}
		}
		if (docJson.get(Constant.DOCTOR_LOCATION) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.DOCTOR_LOCATION)))
			this.setDoctor_location((String) docJson.get(Constant.DOCTOR_LOCATION));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.TELEPHONE_NOS) != null && !JSONObject.NULL.equals(docJson.get(Constant.TELEPHONE_NOS)))
			this.setTelephone_nos((String) docJson.get(Constant.TELEPHONE_NOS));
		if (docJson.get(Constant.PRIMARY_SPL) != null && !JSONObject.NULL.equals(docJson.get(Constant.PRIMARY_SPL)))
			this.setPrimary_spl((String) docJson.get(Constant.PRIMARY_SPL));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.OTHER_SPLS) != null && !JSONObject.NULL.equals(docJson.get(Constant.OTHER_SPLS)))
			this.setOther_spls((String) docJson.get(Constant.OTHER_SPLS));
		// TODO: Handle Could be Multiple Values; Need this to be a JSON Array
		if (docJson.get(Constant.SUB_SPLS) != null && !JSONObject.NULL.equals(docJson.get(Constant.SUB_SPLS)))
			this.setSub_spls((Integer) docJson.get(Constant.SUB_SPLS));
		if (docJson.get(Constant.ADDRESS1) != null && !JSONObject.NULL.equals(docJson.get(Constant.ADDRESS1)))
			this.setAddress1((String) docJson.get(Constant.ADDRESS1));
		if (docJson.get(Constant.ADDRESS2) != null && !JSONObject.NULL.equals(docJson.get(Constant.ADDRESS2)))
			this.setAddress2((String) docJson.get(Constant.ADDRESS2));
		Constant.log("Set Address 2", 0);
		if (docJson.get(Constant.CITY) != null && !JSONObject.NULL.equals(docJson.get(Constant.CITY)))
			this.setCityname((String) docJson.get(Constant.CITY));
		if (docJson.get(Constant.STATES) != null && !JSONObject.NULL.equals(docJson.get(Constant.STATES))) {
			this.setStatename((String) docJson.get(Constant.STATES));
		}
		if (docJson.get(Constant.COUNTRY_CODE) != null && !JSONObject.NULL.equals(docJson.get(Constant.COUNTRY_CODE)))
			this.setCountry_code((String) docJson.get(Constant.COUNTRY_CODE));
		if (docJson.get(Constant.OVER_ALLRATING) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.OVER_ALLRATING)))
			this.setOver_allrating((Double) docJson.get(Constant.OVER_ALLRATING));
		Constant.log("Set Overall Rating", 0);
		if (docJson.get(Constant.CREATE_DATE) != null && !JSONObject.NULL.equals(docJson.get(Constant.CREATE_DATE))) {
			try {
				this.setCreate_date((Date) docJson.get(Constant.CREATE_DATE));
			} catch (Exception e) {
				e.printStackTrace();
				this.setCreate_date((new java.util.Date(System.currentTimeMillis())));
			}
		}
		if (docJson.get(Constant.DOCACTIVE) != null && !JSONObject.NULL.equals(docJson.get(Constant.DOCACTIVE)))
			this.setDocactive((Integer) docJson.get(Constant.DOCACTIVE));
		if (docJson.get(Constant.PREFIX) != null && !JSONObject.NULL.equals(docJson.get(Constant.PREFIX)))
			this.setPrefix((String) docJson.get(Constant.PREFIX));
		if (docJson.get(Constant.DOCNAME_FIRST) != null && !JSONObject.NULL.equals(docJson.get(Constant.DOCNAME_FIRST)))
			this.setDocname_first((String) docJson.get(Constant.DOCNAME_FIRST));
		if (docJson.get(Constant.DOCNAME_MIDDLE) != null
				&& !JSONObject.NULL.equals(docJson.get(Constant.DOCNAME_MIDDLE)))
			this.setDocname_middle((String) docJson.get(Constant.DOCNAME_MIDDLE));
		if (docJson.get(Constant.DOCNAME_LAST) != null && !JSONObject.NULL.equals(docJson.get(Constant.DOCNAME_LAST)))
			this.setDocname_last((String) docJson.get(Constant.DOCNAME_LAST));
		if (docJson.get(Constant.EMAIL) != null && !JSONObject.NULL.equals(docJson.get(Constant.EMAIL)))
			this.setEmail((String) docJson.get(Constant.EMAIL));
		if (docJson.get(Constant.WAITING_TIME) != null && !JSONObject.NULL.equals(docJson.get(Constant.WAITING_TIME)))
			this.setWaiting_time((Integer) docJson.get(Constant.WAITING_TIME));

		Constant.log("Constructed Doctors Object", 1);
	}

	public Doctors(Integer docid, Integer gender, String edu_training, boolean insurance_accept,
			String hospital_affliated, String board_certifcate, String membership, String awards, Integer waiting_time,
			Date availibity_for_appointment, String doctor_location, String telephone_nos, String primary_spl,
			String other_spls, Integer sub_spls, String address1, String address2, String cityname, String statename,
			String country_code, Double over_allrating, Date create_date, Integer docactive, String prefix,
			String docname_first, String docname_middle, String docname_last, String email, Hospital hospital,
			Specialties specialties, City city, States states, model.countries countries, Integer primary_spl_code,
			Integer other_spls_code, Integer sub_spls_code, String about, Integer city_code, Integer state_code,
			Integer countries_code, Integer hospital_affliated_code, Long rowno, String website_url) {
		super();
		this.docid = docid;
		this.gender = gender;
		this.edu_training = edu_training;
		this.insurance_accept = insurance_accept;
		this.hospital_affliated = hospital_affliated;
		this.board_certifcate = board_certifcate;
		this.membership = membership;
		this.awards = awards;
		this.waiting_time = waiting_time;
		this.availibity_for_appointment = availibity_for_appointment;
		this.doctor_location = doctor_location;
		this.telephone_nos = telephone_nos;
		this.primary_spl = primary_spl;
		this.other_spls = other_spls;
		this.sub_spls = sub_spls;
		this.address1 = address1;
		this.address2 = address2;
		this.cityname = cityname;
		this.statename = statename;
		this.country_code = country_code;
		this.over_allrating = over_allrating;
		this.create_date = create_date;
		this.docactive = docactive;
		this.prefix = prefix;
		this.docname_first = docname_first;
		this.docname_middle = docname_middle;
		this.docname_last = docname_last;
		this.email = email;
		this.hospital = hospital;
		this.specialties = specialties;
		this.city = city;
		this.states = states;
		this.countries = countries;

		this.primary_spl_code = primary_spl_code;
		this.other_spls_code = other_spls_code;
		this.sub_spls_code = sub_spls_code;
		this.about = about;
		this.city_code = city_code;
		this.state_code = state_code;
		this.countries_code = countries_code;
		this.hospital_affliated_code = hospital_affliated_code;
		this.rowno = rowno;
		this.website_url = website_url;
	}

	public int getDocid() {
		return docid;
	}

	public void setDocid(int docid) {
		this.docid = docid;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getEdu_training() {
		return edu_training;
	}

	public void setEdu_training(String edu_training) {
		this.edu_training = edu_training;
	}

	public boolean isInsurance_accept() {
		return insurance_accept;
	}

	public void setInsurance_accept(boolean insurance_accept) {
		this.insurance_accept = insurance_accept;
	}

	public String getHospital_affliated() {
		return hospital_affliated;
	}

	public void setHospital_affliated(String hospital_affliated) {
		this.hospital_affliated = hospital_affliated;
	}

	public String getBoard_certifcate() {
		return board_certifcate;
	}

	public void setBoard_certifcate(String board_certifcate) {
		this.board_certifcate = board_certifcate;
	}

	public String getMembership() {
		return membership;
	}

	public void setMembership(String membership) {
		this.membership = membership;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public int getWaiting_time() {
		return waiting_time;
	}

	public void setWaiting_time(int waiting_time) {
		this.waiting_time = waiting_time;
	}

	public Date getAvailibity_for_appointment() {
		return availibity_for_appointment;
	}

	public void setAvailibity_for_appointment(Date availibity_for_appointment) {
		this.availibity_for_appointment = availibity_for_appointment;
	}

	public String getDoctor_location() {
		return doctor_location;
	}

	public void setDoctor_location(String doctor_location) {
		this.doctor_location = doctor_location;
	}

	public String getTelephone_nos() {
		return telephone_nos;
	}

	public void setTelephone_nos(String telephone_nos) {
		this.telephone_nos = telephone_nos;
	}

	public String getPrimary_spl() {
		return primary_spl;
	}

	public void setPrimary_spl(String primary_spl) {
		this.primary_spl = primary_spl;
	}

	public String getOther_spls() {
		return other_spls;
	}

	public void setOther_spls(String other_spls) {
		this.other_spls = other_spls;
	}

//TODO: This should be a JSON Array of ID's
	public int getSub_spls() {
		return sub_spls;
	}

	public void setSub_spls(int sub_spls) {
		this.sub_spls = sub_spls;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public double getOver_allrating() {
		return over_allrating;
	}

	public void setOver_allrating(double over_allrating) {
		this.over_allrating = over_allrating;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public int getDocactive() {
		return docactive;
	}

	public void setDocactive(int docactive) {
		this.docactive = docactive;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDocname_first() {
		return docname_first;
	}

	public void setDocname_first(String docname_first) {
		this.docname_first = docname_first;
	}

	public String getDocname_middle() {
		return docname_middle;
	}

	public void setDocname_middle(String docname_middle) {
		this.docname_middle = docname_middle;
	}

	public String getDocname_last() {
		return docname_last;
	}

	public void setDocname_last(String docname_last) {
		this.docname_last = docname_last;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Specialties getSpecialties() {
		return specialties;
	}

	public void setSpecialties(Specialties specialties) {
		this.specialties = specialties;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public States getStates() {
		return states;
	}

	public void setStates(States states) {
		this.states = states;
	}

	public countries getCountries() {
		return countries;
	}

	public void setCountries(countries countries) {
		this.countries = countries;
	}

	public Integer getPrimary_spl_code() {
		return primary_spl_code;
	}

	public void setPrimary_spl_code(Integer primary_spl_code) {
		this.primary_spl_code = primary_spl_code;
	}

	public Integer getOther_spls_code() {
		return other_spls_code;
	}

	public void setOther_spls_code(Integer other_spls_code) {
		this.other_spls_code = other_spls_code;
	}

	public Integer getSub_spls_code() {
		return sub_spls_code;
	}

	public void setSub_spls_code(Integer sub_spls_code) {
		this.sub_spls_code = sub_spls_code;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Integer getCity_code() {
		return city_code;
	}

	public void setCity_code(Integer city_code) {
		this.city_code = city_code;
	}

	public Integer getState_code() {
		return state_code;
	}

	public void setState_code(Integer state_code) {
		this.state_code = state_code;
	}

	public Integer getCountries_code() {
		return countries_code;
	}

	public void setCountries_code(Integer countries_code) {
		this.countries_code = countries_code;
	}

	public Integer getHospital_affliated_code() {
		return hospital_affliated_code;
	}

	public void setHospital_affliated_code(Integer hospital_affliated_code) {
		this.hospital_affliated_code = hospital_affliated_code;
	}

	public Long getRowno() {
		return rowno;
	}

	public void setRowno(Long rowno) {
		this.rowno = rowno;
	}
	
	public String getWebsite_url() {
		return website_url;
	}

	public void setWebsite_url(String website_url) {
		this.website_url = website_url;
	}

}
