package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import util.Constant;

@Entity

@Table(name=Constant.HOSPITAL)
public class Hospital {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer hospitalid;
	
	@OneToMany(mappedBy=Constant.HOSPITAL , cascade= CascadeType.ALL)
	private Set<Doctors> doctors;
	
	@Column
	private String hospital_affliated;
	
	
	

	public Hospital() {
		super();
		// TODO Auto-generated constructor stub
	}




	public Hospital(Integer hospitalid, Set<Doctors> doctors, String hospital_affliated) {
		super();
		this.hospitalid = hospitalid;
		this.doctors = doctors;
		this.hospital_affliated = hospital_affliated;
	}




	public Integer getHospitalid() {
		return hospitalid;
	}




	public void setHospitalid(Integer hospitalid) {
		this.hospitalid = hospitalid;
	}




	public Set<Doctors> getDoctors() {
		return doctors;
	}




	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}




	public String getHospital_affliated() {
		return hospital_affliated;
	}




	public void setHospital_affliated(String hospital_affliated) {
		this.hospital_affliated = hospital_affliated;
	}
	

}