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
@Table(name = "copyright")
public class Copyright implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "copyright_id")
	private Integer copyright_id;
	@Column(name = "copyright_file_loc")
	private String copyright_file_loc;
	@Column(name = "copyright_create_date")
	private Date copyright_create_date;
	@Column(name = "copyright_effective_date")
	private Date copyright_effective_date;
	@Column(name = "copyright_expiration_date")
	private Date copyright_expiration_date;
	@Column(name = "copyright_status")
	private Integer copyright_status;
	public Copyright() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Copyright(Integer copyright_id, String copyright_file_loc, Date copyright_create_date,
			Date copyright_effective_date, Date copyright_expiration_date, Integer copyright_status) {
		super();
		this.copyright_id = copyright_id;
		this.copyright_file_loc = copyright_file_loc;
		this.copyright_create_date = copyright_create_date;
		this.copyright_effective_date = copyright_effective_date;
		this.copyright_expiration_date = copyright_expiration_date;
		this.copyright_status = copyright_status;
	}
	public Integer getCopyright_id() {
		return copyright_id;
	}
	public void setCopyright_id(Integer copyright_id) {
		this.copyright_id = copyright_id;
	}
	public String getCopyright_file_loc() {
		return copyright_file_loc;
	}
	public void setCopyright_file_loc(String copyright_file_loc) {
		this.copyright_file_loc = copyright_file_loc;
	}
	public Date getCopyright_create_date() {
		return copyright_create_date;
	}
	public void setCopyright_create_date(Date copyright_create_date) {
		this.copyright_create_date = copyright_create_date;
	}
	public Date getCopyright_effective_date() {
		return copyright_effective_date;
	}
	public void setCopyright_effective_date(Date copyright_effective_date) {
		this.copyright_effective_date = copyright_effective_date;
	}
	public Date getCopyright_expiration_date() {
		return copyright_expiration_date;
	}
	public void setCopyright_expiration_date(Date copyright_expiration_date) {
		this.copyright_expiration_date = copyright_expiration_date;
	}
	public Integer getCopyright_status() {
		return copyright_status;
	}
	public void setCopyright_status(Integer copyright_status) {
		this.copyright_status = copyright_status;
	}
}
