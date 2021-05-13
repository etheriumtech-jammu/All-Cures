package model;

import java.util.Date;

public class Subspecialties {
	private Integer ssplid;

	private String sspl_name;

	private String created_by;

	private Date create_date;

	private boolean ssplactive;





	public Subspecialties() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Subspecialties(Integer ssplid, String sspl_name, String created_by, Date create_date, boolean ssplactive) {
		super();
		this.ssplid = ssplid;
		this.sspl_name = sspl_name;
		this.created_by = created_by;
		this.create_date = create_date;
		this.ssplactive = ssplactive;
	}

	public Integer getSsplid() {
		return ssplid;
	}

	public void setSsplid(Integer ssplid) {
		this.ssplid = ssplid;
	}

	public String getSspl_name() {
		return sspl_name;
	}

	public void setSspl_name(String sspl_name) {
		this.sspl_name = sspl_name;
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

	public boolean isSsplactive() {
		return ssplactive;
	}

	public void setSsplactive(boolean ssplactive) {
		this.ssplactive = ssplactive;
	}



}
