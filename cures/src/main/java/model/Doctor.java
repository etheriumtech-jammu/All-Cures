package model;



import org.apache.solr.client.solrj.beans.Field;

public class Doctor {
	
	@Field public String doctorid ;
	@Field public Integer gender;
	@Field public String edu_training;
	@Field public String hospital_affliated;
	@Field public Integer insurance_accept;
	@Field public String board_certifcate;
	@Field public String membership;
	@Field public String awards;
	@Field public String availibity_for_appointment;
	@Field public String doctor_location;
	@Field public String telephone_nos;
	@Field public String primary_spl;
	@Field public String other_spls;
	@Field public String sub_spls;
	@Field public String address1;
	@Field public String address2;
	@Field public String city;
	@Field public String state;
	@Field public String country_code;
	@Field public String over_allrating;
	@Field public String create_date;
	@Field public Integer docactive;
	@Field public String prefix;
	@Field public String docname_first;
	@Field public String docname_middle;
	@Field public String docname_last;
	@Field public String name;
	@Field public String email;
	@Field public String waiting_time;
	@Field public String pincode;
	@Field public String latlon;
	@Field public String rowno ;

	
	public Doctor(String doctorid, Integer gender, String edu_training, String hospital_affliated,
			Integer insurance_accept, String board_certifcate, String membership, String awards,
			String availibity_for_appointment, String doctor_location, String telephone_nos, String primary_spl,
			String other_spls, String sub_spls, String address1, String address2, String city, String state,
			String country_code, String over_allrating, String create_date, Integer docactive, String prefix,
			String docname_first, String docname_middle, String docname_last, String name, String email, String waiting_time,
			String pincode, String latlon, String rowno) {
		super();
		this.doctorid = doctorid;
		this.gender = gender;
		this.edu_training = edu_training;
		this.hospital_affliated = hospital_affliated;
		this.insurance_accept = insurance_accept;
		this.board_certifcate = board_certifcate;
		this.membership = membership;
		this.awards = awards;
		this.availibity_for_appointment = availibity_for_appointment;
		this.doctor_location = doctor_location;
		this.telephone_nos = telephone_nos;
		this.primary_spl = primary_spl;
		this.other_spls = other_spls;
		this.sub_spls = sub_spls;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.country_code = country_code;
		this.over_allrating = over_allrating;
		this.create_date = create_date;
		this.docactive = docactive;
		this.prefix = prefix;
		this.docname_first = docname_first;
		this.docname_middle = docname_middle;
		this.docname_last = docname_last;
		this.name = name;
		this.email = email;
		this.waiting_time = waiting_time;
		this.pincode = pincode;
		this.latlon = latlon;
		this.rowno = rowno;
		
	}
	
	//new sor document with lesser fields
	public Doctor(String doctorid, Integer gender, String edu_training, String hospital_affliated,
			String telephone_nos, String primary_spl,
			String sub_spls, String address1, String city, String state,
			String country_code, String over_allrating, String prefix,
			String docname_first, String docname_middle, String docname_last, String name, String email,
			String pincode, String latlon, String rowno) {
		super();
		this.doctorid = doctorid;
		this.gender = gender;
		this.edu_training = edu_training;
		this.hospital_affliated = hospital_affliated;
		this.insurance_accept = insurance_accept;
		this.telephone_nos = telephone_nos;
		this.primary_spl = primary_spl;
		this.sub_spls = sub_spls;
		this.address1 = address1;
		this.city = city;
		this.state = state;
		this.country_code = country_code;
		this.over_allrating = over_allrating;
		this.prefix = prefix;
		this.docname_first = docname_first;
		this.docname_middle = docname_middle;
		this.docname_last = docname_last;
		this.name = name;
		this.email = email;
		this.pincode = pincode;
		this.latlon = latlon;
		this.rowno = rowno;
		
	}
	
	public String getRowno() {
		return rowno;
	}

	public void setRowno(String rowno) {
		this.rowno = rowno;
	}

	public Doctor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDoctorid() {
		return doctorid;
	}
	public void setDoctorid(String doctorid) {
		this.doctorid = doctorid;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getEdu_training() {
		return edu_training;
	}
	public void setEdu_training(String edu_training) {
		this.edu_training = edu_training;
	}
	public String getHospital_affliated() {
		return hospital_affliated;
	}
	public void setHospital_affliated(String hospital_affliated) {
		this.hospital_affliated = hospital_affliated;
	}
	public Integer getInsurance_accept() {
		return insurance_accept;
	}
	public void setInsurance_accept(Integer insurance_accept) {
		this.insurance_accept = insurance_accept;
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
	public String getAvailibity_for_appointment() {
		return availibity_for_appointment;
	}
	public void setAvailibity_for_appointment(String availibity_for_appointment) {
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
	public String getSub_spls() {
		return sub_spls;
	}
	public void setSub_spls(String sub_spls) {
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getOver_allrating() {
		return over_allrating;
	}
	public void setOver_allrating(String over_allrating) {
		this.over_allrating = over_allrating;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public Integer getDocactive() {
		return docactive;
	}
	public void setDocactive(Integer docactive) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWaiting_time() {
		return waiting_time;
	}
	public void setWaiting_time(String waiting_time) {
		this.waiting_time = waiting_time;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLatlon() {
		return latlon;
	}

	public void setLat(String lat) {
		this.latlon = latlon;
	}
	
}
