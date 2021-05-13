package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name="ratedbytype")
public class Ratedbytype {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer ratedBy_id;
	@Column
	private String ratedBy_name;
	public Ratedbytype() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ratedbytype(Integer ratedBy_id, String ratedBy_name) {
		super();
		this.ratedBy_id = ratedBy_id;
		this.ratedBy_name = ratedBy_name;
	}
	public Integer getRatedBy_id() {
		return ratedBy_id;
	}
	public void setRatedBy_id(Integer ratedBy_id) {
		this.ratedBy_id = ratedBy_id;
	}
	public String getRatedBy_name() {
		return ratedBy_name;
	}
	public void setRatedBy_name(String ratedBy_name) {
		this.ratedBy_name = ratedBy_name;
	}
	

}
