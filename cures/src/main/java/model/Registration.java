package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.Constant;;


@Entity
@Table(name = Constant.REGISTRATION)
public class Registration implements Serializable {
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constant.REGISTRATION_ID)
	private Integer registration_id;
	 @Column(name = Constant.FIRST_NAME)
	private String first_name;
	 @Column(name = Constant.LAST_NAME)
	private String last_name;
	 @Column(name = Constant.EMAIL_ADDRESS)
	private String email_address;
	 @Column(name = Constant.PASS_WORD)
	private String  pass_word;
	 @Column(name = Constant.REGISTRATION_TYPE)
	private Integer registration_type;
	 @Column(name = Constant.ACCEPTANCE_CONDITION)
	private Boolean acceptance_condition;
	 
	 @Column(name = Constant.PRIVACY_POLICY)
	private Boolean privacy_policy;
	@Column(name = Constant.ACCOUNT_STATE)
	private Integer account_state;
	 @Column(name = Constant.REMEMBER_ME)
	private Integer remember_me;
	 
	 @Column(name = "mobile_number")
	 private Long mobile_number;
	 
	 @Column(name = Constant.LOGIN_ATTEMPT)
	 private Integer login_attempt;
	 @Column(name = Constant.LAST_LOGIN_DATETIME)
	 private Date last_login_datatime;
	
	 @Column(name = Constant.ROWNO)
	 private Long rowno;
	
	public Registration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Registration(Integer registration_id, String first_name, String last_name, String email_address,
			String pass_word, Integer registration_type, Boolean acceptance_condition, Boolean privacy_policy,
			Integer account_state, Integer remember_me, Integer login_attempt, Date last_login_datatime, Long mobile_number, Long rowno) {
		super();
		this.registration_id = registration_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email_address = email_address;
		this.pass_word = pass_word;
		this.registration_type = registration_type;
		this.acceptance_condition = acceptance_condition;
		this.privacy_policy= privacy_policy;
		this.account_state = account_state;
		this.remember_me = remember_me;
		this.login_attempt = login_attempt;
		this.last_login_datatime = last_login_datatime;
		this.mobile_number = mobile_number;
		this.rowno = rowno;
	}
	public Integer getRegistration_id() {
		return registration_id;
	}
	public void setRegistration_id(Integer registration_id) {
		this.registration_id = registration_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getPass_word() {
		return pass_word;
	}
	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}
	public Integer getRegistration_type() {
		return registration_type;
	}
	public void setRegistration_type(Integer registration_type) {
		this.registration_type = registration_type;
	}
	public Boolean getAcceptance_condition() {
		return acceptance_condition;
	}
	public void setAcceptance_condition(Boolean acceptance_condition) {
		this.acceptance_condition = acceptance_condition;
	}
	public Boolean getprivacy_policy() {
		return privacy_policy;
	}
	public void setprivacy_policy(Boolean privacy_policy) {
		this.privacy_policy = privacy_policy;
	}
	public Integer getAccount_state() {
		return account_state;
	}
	public void setAccount_state(Integer account_state) {
		this.account_state = account_state;
	}
	public Integer getRemember_me() {
		return remember_me;
	}
	public void setRemember_me(Integer remember_me) {
		this.remember_me = remember_me;
	}
	public Long getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(Long mobile_number) {
		this.mobile_number = mobile_number;
	}
	public Integer getLogin_attempt() {
		return login_attempt;
	}
	public void setLogin_attempt(Integer login_attempt) {
		this.login_attempt = login_attempt;
	}
	public Date getLast_login_datatime() {
		return last_login_datatime;
	}
	public void setLast_login_datatime(Date last_login_datatime) {
		this.last_login_datatime = last_login_datatime;
	}
	
	public Long getRowno() {
		return rowno;
	}
	public void setRowno(Long rowno) {
		this.rowno = rowno;
	}
	
	

}
