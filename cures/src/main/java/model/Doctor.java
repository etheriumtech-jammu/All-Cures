package model;

import org.apache.solr.client.solrj.beans.Field;

public class Doctor {
	
	@Field public String docID ;
	@Field public Integer gender;
	@Field public String hospitalAffiliated;
	@Field public Integer insuranceAccept;	
	@Field public String awards;
	@Field public String telephoneNos;
	@Field public String primarySpl;
	@Field public String otherSpl;
	@Field public String address1;
	@Field public String address2;
	@Field public String city;
	@Field public String state;
	@Field public String country;
	@Field public String overAllRating;
	@Field public String createdDate;
	@Field public String prefix;
	@Field public String firstName;
	@Field public String middleName;
	@Field public String lastName;
	@Field public String fullName;
	@Field public String email;
	@Field public String waitingTime;
	@Field public String about ;
	@Field public String imgLoc ;
	@Field public Integer docActive ;
	@Field public String medicineType ;
	public Doctor(String DocID, Integer gender, String hospital_affliated,
			Integer insurance_accept, String board_certifcate, String membership, 
			String telephone_nos, String primary_spl,
			String other_spls, String sub_spls, String address1, String address2, String city, String state,
			String country_code, String over_allrating, String create_date, Integer docactive, String prefix,
			String docname_first, String docname_middle, String docname_last, String name, String email, String waiting_time,
		 String about,String img_Loc,String MedicineType) {
		super();
		this.docID = DocID;
		this.gender = gender;
		this.imgLoc=img_Loc;
		this.hospitalAffiliated = hospital_affliated;
		this.insuranceAccept = insurance_accept;
		
		this.telephoneNos = telephone_nos;
		this.primarySpl = primary_spl;
		this.otherSpl = other_spls;
	
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.country = country_code;
		this.overAllRating = over_allrating;
		this.createdDate = create_date;
		this.docActive = docactive;
		this.prefix = prefix;
		this.firstName = docname_first;
		this.middleName = docname_middle;
		this.lastName = docname_last;
		this.fullName = name;
		this.email = email;
		this.waitingTime = waiting_time;
		this.medicineType=MedicineType;
		this.about = about;
		
	}
	
	//new sor document with lesser fields
	public Doctor(String DocID, Integer gender, String hospital_affliated,
			String telephone_nos, String primary_spl, String address1, String city, String state,
			String country_code, String over_allrating, String prefix,
			String docname_first, String docname_middle, String docname_last, String name, String email,
			 String about,String img_Loc,String MedicineType) {
		super();
		this.docID = DocID;
		this.gender = gender;
		
		this.hospitalAffiliated = hospital_affliated;
		
		this.telephoneNos = telephone_nos;
		this.primarySpl = primary_spl;
		this.imgLoc=img_Loc;
		this.address1 = address1;
		this.city = city;
		this.state = state;
		this.country = country_code;
		this.overAllRating = over_allrating;
		this.prefix = prefix;
		this.firstName = docname_first;
		this.middleName = docname_middle;
		this.lastName = docname_last;
		this.fullName = name;
		this.email = email;
		this.about = about;
		this.medicineType=MedicineType;
	}
	
	

	public Doctor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDocID() {
		return docID;
	}

	public void setDocID(String DocID) {
		this.docID = DocID;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getHospitalAffiliated() {
		return hospitalAffiliated;
	}

	public void setHospitalAffiliated(String hospitalAffliated) {
		this.hospitalAffiliated = hospitalAffliated;
	}

	public Integer getInsuranceAccept() {
		return insuranceAccept;
	}

	public void setInsuranceAccept(Integer insuranceAccept) {
		this.insuranceAccept = insuranceAccept;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public String getTelephoneNos() {
		return telephoneNos;
	}

	public void setTelephoneNos(String telephoneNos) {
		this.telephoneNos = telephoneNos;
	}

	public String getPrimarySpl() {
		return primarySpl;
	}

	public void setPrimarySpl(String primarySpl) {
		this.primarySpl = primarySpl;
	}

	public String getOtherSpl() {
		return otherSpl;
	}

	public void setOtherSpl(String otherSpl) {
		this.otherSpl = otherSpl;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOverAllRating() {
		return overAllRating;
	}

	public void setOverAllRating(String overAllRating) {
		this.overAllRating = overAllRating;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(String waitingTime) {
		this.waitingTime = waitingTime;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getImgLoc() {
		return imgLoc;
	}

	public void setImgLoc(String imgLoc) {
		this.imgLoc = imgLoc;
	}

	public Integer getDocActive() {
		return docActive;
	}

	public void setDocActive(Integer docActive) {
		this.docActive = docActive;
	}
	public String getMedicineType() {
		return medicineType;
	}

	public void setMedicineType(String MedicineType) {
		
		medicineType = MedicineType;
		
	}
	
	
}
