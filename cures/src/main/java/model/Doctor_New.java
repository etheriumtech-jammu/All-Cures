package model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Doctors_New")
public class Doctor_New {

    public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getEduTraining() {
		return eduTraining;
	}

	public void setEduTraining(String eduTraining) {
		this.eduTraining = eduTraining;
	}

	public boolean isInsuranceAccept() {
		return insuranceAccept;
	}

	public void setInsuranceAccept(boolean insuranceAccept) {
		this.insuranceAccept = insuranceAccept;
	}

	public int getHospitalAffiliated() {
		return hospitalAffiliated;
	}

	public void setHospitalAffiliated(int hospitalAffiliated) {
		this.hospitalAffiliated = hospitalAffiliated;
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

	public int getPrimary_spl() {
		return primary_spl;
	}

	public void setPrimary_spl(int primary_spl) {
		this.primary_spl = primary_spl;
	}

	public String getOther_spls() {
		return other_spls;
	}

	public void setOther_spls(String other_spls) {
		this.other_spls = other_spls;
	}

	public float getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(float overallRating) {
		this.overallRating = overallRating;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isDocActive() {
		return docActive;
	}

	public void setDocActive(boolean docActive) {
		this.docActive = docActive;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Date getFeaturedDoctorDate() {
		return featuredDoctorDate;
	}

	public void setFeaturedDoctorDate(Date featuredDoctorDate) {
		this.featuredDoctorDate = featuredDoctorDate;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public Date getNationalRegistrationDate() {
		return nationalRegistrationDate;
	}

	public void setNationalRegistrationDate(Date nationalRegistrationDate) {
		this.nationalRegistrationDate = nationalRegistrationDate;
	}

	public int getRegistrationWithStateBoardID() {
		return registrationWithStateBoardID;
	}

	public void setRegistrationWithStateBoardID(int registrationWithStateBoardID) {
		this.registrationWithStateBoardID = registrationWithStateBoardID;
	}

	public String getNationalRegistrationNumber() {
		return nationalRegistrationNumber;
	}

	public void setNationalRegistrationNumber(String nationalRegistrationNumber) {
		this.nationalRegistrationNumber = nationalRegistrationNumber;
	}

	public int getMedicineTypeId() {
		return medicineTypeId;
	}

	public void setMedicineTypeId(int medicineTypeId) {
		this.medicineTypeId = medicineTypeId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docid")
   private int docID;

    @Column(name = "gender")
    private int gender;

    @Column(name = "edu_training")
    private String eduTraining;

    @Column(name = "insurance_accept")
    private boolean insuranceAccept;

    @Column(name = "hospital_affiliated")
    private int hospitalAffiliated;

    @Column(name = "awards")
    private String awards;

    @Column(name = "telephone_nos")
    private String telephoneNos;

    @Column(name = "primary_spl")
    private int primary_spl;

    @Column(name = "other_spls")
    private String other_spls;

    @Column(name = "over_allrating")
    private float overallRating;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "docname_first")
    private String firstName;

    @Column(name = "docname_middle")
    private String middleName;

    @Column(name = "docname_last")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "waiting_time")
    private int waitingTime;

    @Column(name = "verified")
    private int verified;

    @Column(name = "about", length = 3000)
    private String about;

    @Column(name = "docactive")
    private boolean docActive;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "featured_doctor_date")
    private Date featuredDoctorDate;

    @Column(name = "img_Loc")
    private String imageLocation;

    @Column(name = "Natl_Reg_Date")
    private Date nationalRegistrationDate;

    @Column(name = "RegWithStateBoardID")
    private int registrationWithStateBoardID;

    @Column(name = "NatlRegNo")
    private String nationalRegistrationNumber;

    @Column(name = "MedicineTypeID")
    private int medicineTypeId;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "CreatedBy")
    private int createdBy;

    @Column(name = "UpdatedBy")
    private int updatedBy;

    @Column(name = "Status")
    private int status;

    @Transient
    private String Hospital_Affiliated;
	
	@Transient
    private String Primary_Spl;
	
	@Transient
    private String RegBoardState;
	
	@Transient
    private String MedicineType;
	
	@Transient
    private String Address1;
	
	@Transient
    private String Address2;
	
	@Transient
    private String AddressType;
	
	@Transient
    private String City;
	
	@Transient
    private String State;
	
	@Transient
    private String Country;
	
	@Transient
    private String DegDesc;
	
	@Transient
    private int YearofGrad;
	
	@Transient
    private String UnivName;
	
	@Transient
    private String UnivCity;
	
	@Transient
    private String UnivState;
	
	@Transient
    private String UnivCountry;

	public String getHospital_Affiliated() {
		return Hospital_Affiliated;
	}

	public void setHospital_Affiliated(String hospital_Affiliated) {
		Hospital_Affiliated = hospital_Affiliated;
	}

	public String getPrimary_Spl() {
		return Primary_Spl;
	}

	public void setPrimary_Spl(String primary_Spl) {
		Primary_Spl = primary_Spl;
	}

	public String getRegBoardState() {
		return RegBoardState;
	}

	public void setRegBoardState(String regBoardState) {
		RegBoardState = regBoardState;
	}

	public String getMedicineType() {
		return MedicineType;
	}

	public void setMedicineType(String medicineType) {
		MedicineType = medicineType;
	}

	public String getAddress1() {
		return Address1;
	}

	public void setAddress1(String address1) {
		Address1 = address1;
	}

	public String getAddress2() {
		return Address2;
	}

	public void setAddress2(String address2) {
		Address2 = address2;
	}

	public String getAddressType() {
		return AddressType;
	}

	public void setAddressType(String addressType) {
		AddressType = addressType;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getDegDesc() {
		return DegDesc;
	}

	public void setDegDesc(String degDesc) {
		DegDesc = degDesc;
	}

	public int getYearofGrad() {
		return YearofGrad;
	}

	public void setYearofGrad(int yearofGrad) {
		YearofGrad = yearofGrad;
	}

	public String getUnivName() {
		return UnivName;
	}

	public void setUnivName(String univName) {
		UnivName = univName;
	}

	public String getUnivCity() {
		return UnivCity;
	}

	public void setUnivCity(String univCity) {
		UnivCity = univCity;
	}

	public String getUnivState() {
		return UnivState;
	}

	public void setUnivState(String univState) {
		UnivState = univState;
	}

	public String getUnivCountry() {
		return UnivCountry;
	}

	public void setUnivCountry(String univCountry) {
		UnivCountry = univCountry;
	}
	
	
    // Constructors, getters, setters, and other methods

}
