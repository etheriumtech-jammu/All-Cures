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

@Table(name=Constant.CITYVALUE)
public class City {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer citycode;
	@Column
	private String cityname;
	@Column
	private Long pincode;
	@Column
	private Integer country_code;
	@Column
	private Integer state_code;
	
	@OneToMany(mappedBy=Constant.CITYVALUE , cascade= CascadeType.ALL)
	private Set<Doctors> doctors;
	public City() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public City(Integer citycode, String cityname, Long pincode, Integer country_code, Integer state_code,
			Set<Doctors> doctors) {
		super();
		this.citycode = citycode;
		this.cityname = cityname;
		this.pincode = pincode;
		this.country_code = country_code;
		this.state_code = state_code;
		this.doctors = doctors;
	}


	public Integer getCitycode() {
		return citycode;
	}
	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public Long getPincode() {
		return pincode;
	}
	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}
	public Integer getCountry_code() {
		return country_code;
	}
	public void setCountry_code(Integer country_code) {
		this.country_code = country_code;
	}
	public Integer getState_code() {
		return state_code;
	}
	public void setState_code(Integer state_code) {
		this.state_code = state_code;
	}
	public Set<Doctors> getDoctors() {
		return doctors;
	}
	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}

	

}
