package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.Constant;

@Entity

@Table(name="targetbytype")
public class Targetbytype {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer target_type_id;
	@Column
	private String target_type_name;
	public Targetbytype() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Targetbytype(Integer target_type_id, String target_type_name) {
		super();
		this.target_type_id = target_type_id;
		this.target_type_name = target_type_name;
	}
	public Integer getTarget_type_id() {
		return target_type_id;
	}
	public void setTarget_type_id(Integer target_type_id) {
		this.target_type_id = target_type_id;
	}
	public String getTarget_type_name() {
		return target_type_name;
	}
	public void setTarget_type_name(String target_type_name) {
		this.target_type_name = target_type_name;
	}
	
	
	

}
