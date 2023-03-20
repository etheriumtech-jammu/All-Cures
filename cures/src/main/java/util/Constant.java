package util;

public class Constant{
	public static final String ADDRESS ="127.0.0.1:11211";
	public static final String SolrServer = "http://localhost:8983/solr/new_core";
	public static final String DEFAULT_POSTLOGIN_PAGE = "/cures";
	public static final String LOGIN_FAIL_MSG = "No user found with that Email/Pwd Combination";
	public static final String CITYNAME ="Cityname";
	public static final String PINCODE ="Pincode";
	public static final String CITY = "cityname";
	public static final String PIN = "pincode";
	public static final String ROWNO = "rowno";
	public static final String NULL ="null";
	public static final String DOCID ="docid";
	public static final String GENDER = "gender";
	public static final String EDU_TRAINING ="edu_training";
	public static final String INSURANCE_ACCEPT ="insurance_accept";
	public static final String HOSPITAL_AFFLIATED ="hospital_affliated";
	public static final String BOARD_CERTIFICATION ="board_certifcate";
	public static final String MEMBERSHIP ="membership";
	public static final String AWARDS ="awards";
	public static final String AVAILIBITY_FOR_APPOINTMENT = "availibity_for_appointment";
	public static final String DOCTOR_LOCATION ="doctor_location";
	public static final String TELEPHONE_NOS ="telephone_nos";
	public static final String PRIMARY_SPL ="primary_spl";
	public static final String OTHER_SPLS ="other_spls";
	public static final String SUB_SPLS = "sub_spls";
	public static final String ADDRESS1 ="address1";
	public static final String ADDRESS2 ="address2";
	public static final String CITYVALUE ="city";
	public static final String STATE ="state";
	public static final String COUNTRY_CODE ="country_code";
	public static final String OVER_ALLRATING = "over_allrating";
	public static final String CREATE_DATE = "create_date";
	public static final String DOCACTIVE = "docactive";
	public static final String PREFIX ="prefix";
	public static final String DOCNAME_FIRST ="docname_first";
	public static final String DOCNAME_MIDDLE ="docname_middle";
	public static final String DOCNAME_LAST = "docname_last";
	public static final String EMAIL = "email";
	public static final String WAITING_TIME = "waiting_time";
	public static final String DOCNAME = "docname";
	public static final String SPL = "spl";
	public static final String SUBSPL = "subspl";
	public static final String DOCTORNAME="Doctorname";
	public static final String SPECIALTIES= "Specialties";
	public static final String SUBSPECIALTIES ="SubSpecialties";
	public static final String USER ="user";
	public static final String COOKIE = "cookie";
	public static final String PSW = "psw";
	public static final String REMPWD ="rempwd";
	public static final String MOBILE_NUMBER ="number";
//	public static final String IDCOOK= "idcook";
	public static final String SESSCOOK = "sesscook";
	public static final String ON= "on";
	public static final String OFF="off";
	public static final String MSG="You have successfully logged out.";
	public static final String FIRSTNAME= "firstname";
	public static final String LASTNAME="lastname";
	public static final String PSWREPEAT="psw-repeat";
	public static final String AcceptTermsAndConditions="acceptTnC";
	public static final String AcceptPolicy="acceptPolicy";
	public static final String DOCTOR ="Doctor";
	public static final String PATIENT="Patient";
	public static final String DOCPATIENT="doc_patient";
	public static final String DOCTORS="doctors";
	public static final String DOCTORDETAILS="DoctorDetails";
	public static final String DR = "Dr.";
	//public static final String PREFEX="prefex: ";
	public static final String FIRST_NAME = "first_name";
	public static final String MIDDLE_NAME ="Middle Name : ";
	public static final String LAST_NAME ="last_name" ;
	public static final String OK ="OK";
	public static final String ZERO_RESULTS="ZERO_RESULTS";
	public static final String OR = " or ";
	public static final String NAME="name";
	public static final String AND =" and ";
	public static final String SHA1 = "SHA-1";
	public static final String AES = "AES";
	public static final String PASSWORD = "password";
	public static final String UNUSED="unused";
	public static final String JSONDATA="jsondata";
	public static final String COUNTRIES="countries";
	public static final String HOSPITALID="hospitalid";
	public static final String SPLID = "splid";
	public static final String CITYCODE= "citycode";
	public static final String CODEID="codeid";
	public static final String COUNTRYCODEID = "countrycodeid";
	public static final String HOSPITAL="hospital";
	public static final String REGISTRATION="registration";
	public static final String REGISTRATION_ID="registration_id";
	public static final String EMAIL_ADDRESS= "email_address";
	public static final String PASS_WORD= "pass_word";
	public static final String REGISTRATION_TYPE= "registration_type";
	public static final String ACCEPTANCE_CONDITION= "acceptance_condition";
	public static final String PRIVACY_POLICY= "privacy_policy";
	public static final String ACCOUNT_STATE= "account_state";
	public static final String REMEMBER_ME= "remember_me";
	public static final String LAST_LOGIN_DATETIME= "last_login_datatime";
	public static final String LOGIN_ATTEMPT= "login_attempt";
	public static final String SPECIALTIESTABLE= "specialties";
	public static final String STATES="states";
	public static final String AUTH="Incorrect password";
	public static final String LATITUDELONGITUDE ="latlon";
	public static final String DefaultCookieDomain = "all-cures.com";
	public static final String DefaultSessionCookieName = "acSession";
	public static final String DefaultPermCookieName = "acPerm";
	public static final String DefaultCookiePath = "/";
	public static final String DefaultStringSplitter = "|";
	public static final String CipherAlgorithm = Constant.AES;
	public static final String SECRETE ="secrete";
	public static final int DefaultPermCookieDuration = 31556952; //1 Year represented in seconds
	 
	public static final String SOLRSPLSEARCHJSONDATA = "solrsplsearchjsondata";
	
	public static final String dashboard_review_count = "DBRC";
	
	public static final int login_attempts_max = 5;

	//Logging Related Settings
	public static final boolean LoggingOn = true;
	public static final int ALLOWED_MSG_LVL = 0; //0 = DEBUG; 1 = INFO; 2 = WARN; 3 = ERROR;
	
	public static void log(String msg, int msg_level){
		if(LoggingOn && msg_level >= ALLOWED_MSG_LVL){
			System.out.println(msg);
		}
		return;
	}
}
