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
@Entity
@Table(name = "author")
public class Author implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "author_id")
	private Integer author_id;
	@Column(name = "author_firstname")
	private String	author_firstname;
	@Column(name = "author_middlename")
	private String	author_middlename;
	@Column(name = "author_lastname")
	private String author_lastname;
	@Column(name = "author_email")
	private String author_email;
	@Column(name = "author_address")
	private String author_address;
	@Column(name = "author_telephone")
	private String author_telephone;
	@Column(name = "author_status")
	private Integer author_status;
	
	public Author(){
		super();
	}
	
	public Author(Integer authorid, String authorfirstname, String authormiddlename, String authorlastname,
			String authoremail, String authoraddress, String authortelephone, Integer authorstatus) {
		super();
		this.author_id = authorid;
		this.author_firstname = authorfirstname;
		this.author_middlename = authormiddlename;
		this.author_lastname = authorlastname;
		this.author_email = authoremail;
		this.author_address = authoraddress;
		this.author_telephone = authortelephone;
		this.author_status = authorstatus;
	}
	public Integer getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(Integer author_id) {
		this.author_id = author_id;
	}
	public Integer getAuthor_status() {
		return author_status;
	}
	public void setAuthor_status(Integer authorstatus) {
		this.author_status = authorstatus;
	}
	public String getAuthor_firstname() {
		return author_firstname;
	}
	public void setAuthor_firstname(String author_firstname) {
		this.author_firstname = author_firstname;
	}
	public String getAuthor_middlename() {
		return author_middlename;
	}
	public void setAuthor_middlename(String author_middlename) {
		this.author_middlename = author_middlename;
	}
	public String getAuthor_lastname() {
		return author_lastname;
	}
	public void setAuthor_lastname(String author_lastname) {
		this.author_lastname = author_lastname;
	}
	public String getAuthor_email() {
		return author_email;
	}
	public void setAuthor_email(String author_email) {
		this.author_email = author_email;
	}
	public String getAuthor_address() {
		return author_address;
	}
	public void setAuthor_address(String author_address) {
		this.author_address = author_address;
	}
	public String getAuthor_telephone() {
		return author_telephone;
	}
	public void setAuthor_telephone(String author_telephone) {
		this.author_telephone = author_telephone;
	}
}
