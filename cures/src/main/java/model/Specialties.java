package model;

import java.util.Date;
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
@Table(name= Constant.SPECIALTIESTABLE)
public class Specialties {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer splid;
	@Column
	private String spl_name;
	@Column
	private String created_by;
	@Column
	private Date create_date;
	@Column
	private boolean spl_active;
	@OneToMany(mappedBy=Constant.SPECIALTIESTABLE , cascade= CascadeType.ALL)
	private Set<Doctors> doctors;



	public Specialties() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Specialties(Integer splid, String spl_name, String created_by, Date create_date, boolean spl_active,
			Set<Doctors> doctors) {
		super();
		this.splid = splid;
		this.spl_name = spl_name;
		this.created_by = created_by;
		this.create_date = create_date;
		this.spl_active = spl_active;
		this.doctors = doctors;
	}



	public Integer getSplid() {
		return splid;
	}



	public void setSplid(Integer splid) {
		this.splid = splid;
	}



	public String getSpl_name() {
		return spl_name;
	}



	public void setSpl_name(String spl_name) {
		this.spl_name = spl_name;
	}



	public String getCreated_by() {
		return created_by;
	}



	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}



	public Date getCreate_date() {
		return create_date;
	}



	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}



	public boolean isSpl_active() {
		return spl_active;
	}



	public void setSpl_active(boolean spl_active) {
		this.spl_active = spl_active;
	}



	public Set<Doctors> getDoctors() {
		return doctors;
	}



	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}

	
}