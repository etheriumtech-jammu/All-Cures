package model;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Table (name=Constant.COUNTRIES)

public class countries {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)

	private Integer countrycodeid;

	@Column
	private String countryname;

	@Column
	private String continent_name;

	@OneToMany(mappedBy=Constant.COUNTRIES , cascade=CascadeType.ALL)
	private Set<Doctors> doctors;

	public countries() {
		super();
		// TODO Auto-generated constructor stub
	}

	public countries(Integer countrycodeid, String countryname, String continent_name, Set<Doctors> doctors) {
		super();
		this.countrycodeid = countrycodeid;
		this.countryname = countryname;
		this.continent_name = continent_name;
		this.doctors = doctors;
	}

	

	public Integer getCountrycodeid() {
		return countrycodeid;
	}

	public void setCountrycodeid(Integer countrycodeid) {
		this.countrycodeid = countrycodeid;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getContinent_name() {
		return continent_name;
	}

	public void setContinent_name(String continent_name) {
		this.continent_name = continent_name;
	}

	public Set<Doctors> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}

	



}
