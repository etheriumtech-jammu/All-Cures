package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient implements Serializable {
	@Id
	@Column
	// @GeneratedValue(strategy = GenerationType.AUTO)
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
	private Integer docid;
	@Column
	private String email;
	@Column
	private String about;

	public Patient() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Patient(Integer patient_id, String first_name, String last_name, Integer age, Integer gender, Integer docid,
			String email) {
		super();
		this.patient_id = patient_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.age = age;
		this.gender = gender;
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
