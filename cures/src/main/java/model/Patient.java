package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.Constant;


@Entity
@Table(name = "patient")
public class Patient implements Serializable {
	@Id
	@Column
   //@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer patient_id;
	@Column
	private String first_name;
	@Column
	private String last_name;
	@Column
	private Integer age;
	@Column
	private Integer gender; 
	@Column
	private String disease_case_history;
	@Column
	private String patient_testimony;
	@Column
	private String last_precribtion;
	@Column
	private String teatement_detail;
	@Column
	private Integer docid;
	@Column
	private String email;
	
	
	public Patient() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Patient(Integer patient_id, String first_name, String last_name, Integer age, Integer gender,
			String disease_case_history, String patient_testimony, String last_precribtion, String teatement_detail,
			Integer docid, String email) {
		super();
		this.patient_id = patient_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.age = age;
		this.gender = gender;
		this.disease_case_history = disease_case_history;
		this.patient_testimony = patient_testimony;
		this.last_precribtion = last_precribtion;
		this.teatement_detail = teatement_detail;
		this.docid = docid;
		this.email = email;
	}


	public Integer getPatient_id() {
		return patient_id;
	}


	public void setPatient_id(Integer patient_id) {
		this.patient_id = patient_id;
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


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Integer getGender() {
		return gender;
	}


	public void setGender(Integer gender) {
		this.gender = gender;
	}


	public String getDisease_case_history() {
		return disease_case_history;
	}


	public void setDisease_case_history(String disease_case_history) {
		this.disease_case_history = disease_case_history;
	}


	public String getPatient_testimony() {
		return patient_testimony;
	}


	public void setPatient_testimony(String patient_testimony) {
		this.patient_testimony = patient_testimony;
	}


	public String getLast_precribtion() {
		return last_precribtion;
	}


	public void setLast_precribtion(String last_precribtion) {
		this.last_precribtion = last_precribtion;
	}


	public String getTeatement_detail() {
		return teatement_detail;
	}


	public void setTeatement_detail(String teatement_detail) {
		this.teatement_detail = teatement_detail;
	}


	public Integer getDocid() {
		return docid;
	}


	public void setDocid(Integer docid) {
		this.docid = docid;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	
	
}
