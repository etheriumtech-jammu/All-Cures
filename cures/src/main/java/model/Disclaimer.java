package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name = "disclaimer")
public class Disclaimer implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "disclaimer_id")
	
	private Integer disclaimer_id;
	@Column(name = "disclaimer_file_loc")
	private String disclaimer_file_loc;
	@Column(name = "disclaimer_create_date")
	private Date disclaimer_create_date ; 
	@Column(name = "disclaimer_effective_date")
	private Date disclaimer_effective_date;
	@Column(name = "disclaimer_expiration_date")
	private Date disclaimer_expiration_date;
	@Column(name = "disclaimer_status")
	private Integer disclaimer_status ;
	public Disclaimer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Disclaimer(Integer disclaimer_id, String disclaimer_file_loc, Date disclaimer_create_date,
			Date disclaimer_effective_date, Date disclaimer_expiration_date, Integer disclaimer_status) {
		super();
		this.disclaimer_id = disclaimer_id;
		this.disclaimer_file_loc = disclaimer_file_loc;
		this.disclaimer_create_date = disclaimer_create_date;
		this.disclaimer_effective_date = disclaimer_effective_date;
		this.disclaimer_expiration_date = disclaimer_expiration_date;
		this.disclaimer_status = disclaimer_status;
	}
	public Integer getDisclaimer_id() {
		return disclaimer_id;
	}
	public void setDisclaimer_id(Integer disclaimer_id) {
		this.disclaimer_id = disclaimer_id;
	}
	public String getDisclaimer_file_loc() {
		return disclaimer_file_loc;
	}
	public void setDisclaimer_file_loc(String disclaimer_file_loc) {
		this.disclaimer_file_loc = disclaimer_file_loc;
	}
	public Date getDisclaimer_create_date() {
		return disclaimer_create_date;
	}
	public void setDisclaimer_create_date(Date disclaimer_create_date) {
		this.disclaimer_create_date = disclaimer_create_date;
	}
	public Date getDisclaimer_effective_date() {
		return disclaimer_effective_date;
	}
	public void setDisclaimer_effective_date(Date disclaimer_effective_date) {
		this.disclaimer_effective_date = disclaimer_effective_date;
	}
	public Date getDisclaimer_expiration_date() {
		return disclaimer_expiration_date;
	}
	public void setDisclaimer_expiration_date(Date disclaimer_expiration_date) {
		this.disclaimer_expiration_date = disclaimer_expiration_date;
	}
	public Integer getDisclaimer_status() {
		return disclaimer_status;
	}
	public void setDisclaimer_status(Integer disclaimer_status) {
		this.disclaimer_status = disclaimer_status;
	}
}
