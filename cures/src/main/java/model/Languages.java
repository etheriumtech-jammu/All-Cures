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
@Table(name = "languages")
public class Languages implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "language_id")
	private Integer language_id;
	@Column(name = "lang_name")
	private String lang_name;
	
	public Languages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Languages(Integer language_id, Article article, String lang_name) {
		super();
		this.language_id = language_id;
		this.lang_name = lang_name;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public String getLang_name() {
		return lang_name;
	}

	public void setLang_name(String lang_name) {
		this.lang_name = lang_name;
	}
	
}
