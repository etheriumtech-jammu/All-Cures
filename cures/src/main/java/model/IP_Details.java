package model;

import java.io.Serializable;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import util.Constant;
@Entity
@Table(name = "article_details")

public class IP_Details implements Serializable{
	
	@Id
	
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer reg_id;
	public Integer getReg_id() {
		return reg_id;
	}
	public void setReg_id(Integer reg_id) {
		this.reg_id = reg_id;
	}
	private Integer article_id;
	private String cookie_list;
	private String date;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getArticle_id() {
		return article_id;
	}
	public void setArticle_id(Integer article_id) {
		this.article_id = article_id;
	}
	public String getIp_address() {
		return cookie_list;
	}
	public void setIp_address(String ip_address) {
		this.cookie_list = ip_address;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
}