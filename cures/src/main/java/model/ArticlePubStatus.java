package model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import util.Constant;

@Entity
@Table(name = "articlepubstatus")
public class ArticlePubStatus implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "status_id")
	private Integer status_id;
	
	@Column(name = "status_description")
	private String status_description;
	public ArticlePubStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArticlePubStatus(Integer status_id, Article article, String status_description) {
		super();
		this.status_id = status_id;
		this.status_description = status_description;
	}
	
	public Integer getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}
	
	public String getStatus_description() {
		return status_description;
	}
	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}
	
	

}
