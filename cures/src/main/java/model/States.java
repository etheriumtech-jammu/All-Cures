package model;

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

@Table(name=Constant.STATES)
public class States {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codeid;
	@Column
	 private String statename;
	@Column
	 private String country_code;
	@Column
	 private String continent_name;
	@Column
	 private String pincode;
	@OneToMany(mappedBy=Constant.STATES , cascade= CascadeType.ALL)
	private Set<Doctors> doctors;
	 
	 
	public States() {
		super();
		// TODO Auto-generated constructor stub
	}


	public States(Integer codeid, String statename, String country_code, String continent_name, String pincode,
			Set<Doctors> doctors) {
		super();
		this.codeid = codeid;
		this.statename = statename;
		this.country_code = country_code;
		this.continent_name = continent_name;
		this.pincode = pincode;
		this.doctors = doctors;
	}


	public Integer getCodeid() {
		return codeid;
	}


	public void setCodeid(Integer codeid) {
		this.codeid = codeid;
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


	public String getContinent_name() {
		return continent_name;
	}


	public void setContinent_name(String continent_name) {
		this.continent_name = continent_name;
	}


	public String getPincode() {
		return pincode;
	}


	public void setPincode(String pincode) {
		this.pincode = pincode;
	}


	public Set<Doctors> getDoctors() {
		return doctors;
	}


	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}
	

	
}
