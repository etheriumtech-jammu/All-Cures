package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.Constant;

@Entity

@Table(name="doctorsrating")
public class Doctorsrating {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer rate_id;
	@Column
	private String comments;
	@Column
	private Float ratingVal;
	@Column
	private Integer target_type_id;
	@Column
	private Integer target_id;
	@Column
	private Integer ratedBy_type_id;
	@Column
	private Integer ratedBy_id;



	public Doctorsrating() {
		super();
		// TODO Auto-generated constructor stub
	}
	


public Doctorsrating(Integer rate_id, String comments, Float ratingVal, Integer target_type_id, Integer target_id,
			Integer ratedBy_type_id, Integer ratedBy_id) {
		super();
		this.rate_id = rate_id;
		this.comments = comments;
		this.ratingVal = ratingVal;
		this.target_type_id = target_type_id;
		this.target_id = target_id;
		this.ratedBy_type_id = ratedBy_type_id;
		this.ratedBy_id = ratedBy_id;
	}



public Integer getRate_id() {
	return rate_id;
}



public void setRate_id(Integer rate_id) {
	this.rate_id = rate_id;
}



public String getComments() {
	return comments;
}



public void setComments(String comments) {
	this.comments = comments;
}



public Float getRatingVal() {
	return ratingVal;
}



public void setRatingVal(Float ratingVal) {
	this.ratingVal = ratingVal;
}



public Integer getTarget_type_id() {
	return target_type_id;
}



public void setTarget_type_id(Integer target_type_id) {
	this.target_type_id = target_type_id;
}



public Integer getTarget_id() {
	return target_id;
}



public void setTarget_id(Integer target_id) {
	this.target_id = target_id;
}



public Integer getRatedBy_type_id() {
	return ratedBy_type_id;
}



public void setRatedBy_type_id(Integer ratedBy_type_id) {
	this.ratedBy_type_id = ratedBy_type_id;
}



public Integer getRatedBy_id() {
	return ratedBy_id;
}



public void setRatedBy_id(Integer ratedBy_id) {
	this.ratedBy_id = ratedBy_id;
}


}
