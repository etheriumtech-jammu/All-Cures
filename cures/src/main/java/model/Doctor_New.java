package model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Doctors_New")
public class Doctor_New {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docid")
    private int docID;

    @Column(name = "gender")
    private int gender;

    @Column(name = "insurance_accept")
    private boolean insuranceAccept;

    @Column(name = "hospital_affiliated")
    private int hospitalAffiliatedCode;

    @Column(name = "awards")
    private String awards;

    @Column(name = "telephone_nos")
    private String telephoneNos;

    @Column(name = "primary_spl")
    private int primarySplCode;

    @Column(name = "other_spls")
    private String otherSpl;

    @Column(name = "over_allrating")
    private float overAllRating;

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
    private int docActive;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "featured_doctor_date")
    private Date featuredDoctorDate;

    @Column(name = "Natl_Reg_Date")
    private Date nationalRegistrationDate;

    @Column(name = "RegWithStateBoardID")
    private int registrationWithStateBoardID;

    @Column(name = "NatlRegNo")
    private String nationalRegistrationNumber;

    @Column(name = "MedicineTypeID")
    private int medicineTypeID;

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

    @Column(name = "img_Loc")
    private String imgLoc;

    @Transient
    private String hospitalAffiliated;
	
	@Transient
    private String primarySpl;
	
	@Transient
    private String regBoardState;
	
	@Transient
    private String medicineType;
	
	@Transient
    private String address1;
	
	@Transient
    private String address2;
	
	@Transient
    private String addressType;
	
	@Transient
    private String city;
	
	@Transient
    private String state;
	
	@Transient
    private String country;
	
	@Transient
    private String degDesc;
	
	@Transient
    private int yearOfGrad;
	
	@Transient
    private String univName;
	
	@Transient
    private String univCity;
	
	@Transient
    private String univState;
	
	@Transient
    private String univCountry;


	
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

	public boolean isInsuranceAccept() {
		return insuranceAccept;
	}

	public void setInsuranceAccept(boolean insuranceAccept) {
		this.insuranceAccept = insuranceAccept;
	}

	public int getHospitalAffiliatedCode() {
		return hospitalAffiliatedCode;
	}

	public void setHospitalAffiliatedCode(int hospitalAffiliatedCode) {
		this.hospitalAffiliatedCode = hospitalAffiliatedCode;
	}

	public int getPrimarySplCode() {
		return primarySplCode;
	}

	public void setPrimarySplCode(int primarySplCode) {
		this.primarySplCode = primarySplCode;
	}

	public String getOtherSpl() {
		return otherSpl;
	}

	public void setOtherSpl(String otherSpl) {
		this.otherSpl = otherSpl;
	}

	public float getOverAllRating() {
		return overAllRating;
	}

	public void setOverAllRating(float overAllRating) {
		this.overAllRating = overAllRating;
	}

	public int getMedicineTypeID() {
		return medicineTypeID;
	}

	public void setMedicineTypeID(int medicineTypeID) {
		this.medicineTypeID = medicineTypeID;
	}

	public String getImgLoc() {
		return imgLoc;
	}

	public void setImgLoc(String imgLoc) {
		this.imgLoc = imgLoc;
	}

	public int getDocActive() {
		return docActive;
	}

	public String getHospitalAffiliated() {
		return hospitalAffiliated;
	}

	public void setHospitalAffiliated(String hospitalAffiliated) {
		this.hospitalAffiliated = hospitalAffiliated;
	}

	public String getPrimarySpl() {
		return primarySpl;
	}

	public void setPrimarySpl(String primarySpl) {
		this.primarySpl = primarySpl;
	}

	public String getRegBoardState() {
		return regBoardState;
	}

	public void setRegBoardState(String regBoardState) {
		this.regBoardState = regBoardState;
	}

	public String getMedicineType() {
		return medicineType;
	}

	public void setMedicineType(String medicineType) {
		this.medicineType = medicineType;
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

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
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

	public String getDegDesc() {
		return degDesc;
	}

	public void setDegDesc(String degDesc) {
		this.degDesc = degDesc;
	}

	public String getUnivName() {
		return univName;
	}

	public void setUnivName(String univName) {
		this.univName = univName;
	}

	public String getUnivCity() {
		return univCity;
	}

	public void setUnivCity(String univCity) {
		this.univCity = univCity;
	}

	public String getUnivState() {
		return univState;
	}

	public void setUnivState(String univState) {
		this.univState = univState;
	}

	public String getUnivCountry() {
		return univCountry;
	}

	public void setUnivCountry(String univCountry) {
		this.univCountry = univCountry;
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

	public int isDocActive() {
		return docActive;
	}

	public void setDocActive(int docActive) {
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

	 public int getYearOfGrad() {
		return yearOfGrad;
	}

	public void setYearOfGrad(int yearOfGrad) {
		this.yearOfGrad = yearOfGrad;
	}
    // Constructors, getters, setters, and other methods

}
