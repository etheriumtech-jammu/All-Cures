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

public class Article_dc_name implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "article_id")
	private Integer article_id;
	@Column(name = "title")
	private String title;
	@Column(name = "friendly_name")
	private String friendly_name;
	@Column(name = "subheading")
	private String subheading;
	@Column(name = "content_type")
	private String content_type;
	@Column(name = "keywords")
	private String keywords;
	@Column(name = "window_title")
	private String window_title;
	@Column(name = "content_location")
	private String content_location;
	@Column(name = "authored_by")
	private String authored_by;
	@Column(name = "published_by")
	private Integer published_by;
	@Column(name = "edited_by")
	private Integer edited_by;
	@Column(name = "copyright_id")
	private Integer copyright_id;
	@Column(name = "disclaimer_id")
	private Integer disclaimer_id;
	@Column(name = "create_date")
	private Date create_date;
	@Column(name = "published_date")
	private Date published_date;

	@Column(name = "pubstatus_id")
	private Integer pubstatus_id;

	@Column(name = "language_id")
	private Integer language_id;

	@Column(name = "country_id")
	private Integer country_id;
	
	@Column(name = "comments")
	private String comments;

	@Column(name = "disease_condition_id")
	private Integer disease_condition_id;

	private String content;

	private String dc_name;
	
	
	@Column(name = "type")
	private String type;
	
	
	@Column(name = "over_allrating")
	private Float over_allrating;

	@Column(name = "medicine_type")
	private Integer medicine_type;
	
	@Column(name = "featured_article")
	private String featured_article;
	
	public String getFeatured_article() {
		return featured_article;
	}

	public void setFeatured_article(String featured_article) {
		this.featured_article = featured_article;
	}

	private String authors_name;
	private String reg_type;
	private String reg_doc_pat_id;
	
	private String content_small;
	
	private String medicine_type_name;


	

	/*
	 * @OneToOne(targetEntity = Author.class) private String author_firstname;
	 * 
	 * @OneToOne(targetEntity = Author.class) private String author_middlename;
	 * 
	 * @OneToOne(targetEntity = Author.class) private String author_lastname;
	 * 
	 * @OneToOne(targetEntity = Author.class) private String author_email;
	 * 
	 * @OneToOne(targetEntity = Author.class) private String author_address;
	 * 
	 * @OneToOne(targetEntity = Author.class) private String author_telephone;
	 * 
	 * @OneToOne(targetEntity = Languages.class) private String lang_name;
	 * 
	 * @OneToOne(targetEntity = Copyright.class) private String copyright_file_loc;
	 * 
	 * @OneToOne(targetEntity = Disclaimer.class) private String
	 * disclaimer_file_loc;
	 * 
	 * @OneToOne(targetEntity = ArticlePubStatus.class) private String
	 * status_discription;
	 * 
	 * @OneToOne()
	 * 
	 * @JoinColumn(name="copyright_id", referencedColumnName="copyright_id",
	 * insertable=false, updatable=false) private Copyright copyright;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name="disclaimer_id", referencedColumnName="disclaimer_id",
	 * insertable=false, updatable=false) private Disclaimer disclaimer;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(referencedColumnName="language_id", name="language_id",
	 * insertable=false, updatable=false) private Languages languages;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn (name="pubstatus_id", insertable=false, updatable=false) private
	 * ArticlePubStatus articlePubStatus;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(referencedColumnName="author_id", name="authored_by" ,
	 * insertable=false, updatable=false) private Author author;
	 */

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Article_dc_name() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Article_dc_name(Integer article_id, String title, String friendly_name, String subheading,
			String content_type, String keywords, String window_title, String content_location, Integer lang_id,
			Integer copyright_file_id, Integer disclaimer_file_id, Integer pubStatus, String type, Float over_allrating, String authors_name) {
		super();
		this.article_id = article_id;
		this.title = title;
		this.friendly_name = friendly_name;
		this.subheading = subheading;
		this.content_type = content_type;
		this.keywords = keywords;
		this.window_title = window_title;
		this.content_location = content_location;
		this.type = type;
		this.over_allrating = over_allrating;
		this.authors_name = authors_name;
		/*
		 * this.author_firstname = author_firstname; this.author_middlename =
		 * author_middlename; this.author_lastname = author_lastname; this.author_email
		 * = author_email; this.author_address = author_address; this.author_telephone =
		 * author_telephone; this.lang_name = lang_name; this.copyright_file_loc =
		 * copyright_file_loc; this.disclaimer_file_loc = disclaimer_file_loc;
		 * this.status_discription = status_discription;
		 */
	}

	public Article_dc_name(Integer article_id, String title, String friendly_name, String subheading,
			String contentTypeId, String keywords, String window_title, String content_location, String authored_by,
			Integer published_by, Integer edited_by, Integer copyright_id, Integer disclaimer_id, Date create_date,
			Date published_date, Integer pubstatus_id, Integer language_id, Integer copyright_file_loc_id,
			Integer disclaimer_file_loc_id, Integer country_id, Integer disease_condition_id, String type, Float over_allrating, String authors_name) {
		super();
		this.article_id = article_id;
		this.title = title;
		this.friendly_name = friendly_name;
		this.subheading = subheading;
		this.content_type = contentTypeId;
		this.keywords = keywords;
		this.window_title = window_title;
		this.content_location = content_location;
		this.authored_by = authored_by;
		this.published_by = published_by;
		this.edited_by = edited_by;
		this.copyright_id = copyright_id;
		this.disclaimer_id = disclaimer_id;
		this.create_date = create_date;
		this.published_date = published_date;
		this.pubstatus_id = pubstatus_id;
		this.language_id = language_id;
		this.disease_condition_id = disease_condition_id;
		this.country_id = country_id;
		this.type = type;
		this.over_allrating = over_allrating;
		this.authors_name = authors_name;
		/*
		 * this.copyright_file_loc = copyright_file_loc_id; this.disclaimer_file_loc =
		 * disclaimer_file_loc_id; this.status_discription = status_discription_id;
		 * this.author_firstname = author_firstname; this.author_middlename =
		 * author_middlename; this.author_lastname = author_lastname; this.author_email
		 * = author_email; this.author_address = author_address; this.author_telephone =
		 * author_telephone; this.lang_name = lang_name; this.copyright = copyright;
		 * this.disclaimer = disclaimer; this.languages = languages;
		 * this.articlePubStatus = articlePubStatus; this.author = author;
		 */
	}

	public Integer getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Integer article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFriendly_name() {
		return friendly_name;
	}

	public void setFriendly_name(String friendly_name) {
		this.friendly_name = friendly_name;
	}

	public String getSubheading() {
		return subheading;
	}

	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getWindow_title() {
		return window_title;
	}

	public void setWindow_title(String window_title) {
		this.window_title = window_title;
	}

	public String getContent_location() {
		return content_location;
	}

	public void setContent_location(String content_location) {
		this.content_location = content_location;
	}

	public String getAuthored_by() {
		return authored_by;
	}

	public void setAuthored_by(String authored_by) {
		this.authored_by = authored_by;
	}

	public Integer getPublished_by() {
		return published_by;
	}

	public void setPublished_by(Integer published_by) {
		this.published_by = published_by;
	}

	public Integer getEdited_by() {
		return edited_by;
	}

	public void setEdited_by(Integer edited_by) {
		this.edited_by = edited_by;
	}

	public Integer getCopyright_id() {
		return copyright_id;
	}

	public void setCopyright_id(Integer copyright_id) {
		this.copyright_id = copyright_id;
	}

	public Integer getDisclaimer_id() {
		return disclaimer_id;
	}

	public void setDisclaimer_id(Integer disclaimer_id) {
		this.disclaimer_id = disclaimer_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getPublished_date() {
		return published_date;
	}

	public void setPublished_date(Date published_date) {
		this.published_date = published_date;
	}

	public Integer getPubstatus_id() {
		return pubstatus_id;
	}

	public void setPubstatus_id(Integer pubstatus_id) {
		this.pubstatus_id = pubstatus_id;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public Integer getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}

	public Integer getDisease_condition_id() {
		return disease_condition_id;
	}

	public void setDisease_condition_id(Integer disease_condition_id) {
		this.disease_condition_id = disease_condition_id;
	}

	public String getDc_name() {
		return dc_name;
	}

	public void setDc_name(String dc_name) {
		this.dc_name = dc_name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getOver_allrating() {
		return over_allrating;
	}

	public void setOver_allrating(Float over_allrating) {
		this.over_allrating = over_allrating;
	}

	public String getAuthors_name() {
		return authors_name;
	}

	public void setAuthors_name(String authors_name) {
		this.authors_name = authors_name;
	}

	public String getReg_type() {
		return reg_type;
	}

	public void setReg_type(String reg_type) {
		this.reg_type = reg_type;
	}

	public String getReg_doc_pat_id() {
		return reg_doc_pat_id;
	}

	public void setReg_doc_pat_id(String reg_doc_pat_id) {
		this.reg_doc_pat_id = reg_doc_pat_id;
	}

	public String getContent_small() {
		return content_small;
	}

	public void setContent_small(String content_small) {
		this.content_small = content_small;
	}
	
	public Integer getMedicine_type() {
		return medicine_type;
	}

	public void setMedicine_type(Integer medicine_type) {
		this.medicine_type = medicine_type;
	}

	public String getMedicine_type_name() {
		return medicine_type_name;
	}

	public void setMedicine_type_name(String medicine_type_name) {
		this.medicine_type_name = medicine_type_name;
	}

	/*
	 * public String getAuthor_firstname() { return author_firstname; }
	 * 
	 * 
	 * 
	 * public void setAuthor_firstname(String author_firstname) {
	 * this.author_firstname = author_firstname; }
	 * 
	 * 
	 * 
	 * public String getAuthor_middlename() { return author_middlename; }
	 * 
	 * 
	 * 
	 * public void setAuthor_middlename(String author_middlename) {
	 * this.author_middlename = author_middlename; }
	 * 
	 * 
	 * 
	 * public String getAuthor_lastname() { return author_lastname; }
	 * 
	 * 
	 * 
	 * public void setAuthor_lastname(String author_lastname) { this.author_lastname
	 * = author_lastname; }
	 * 
	 * 
	 * 
	 * public String getAuthor_email() { return author_email; }
	 * 
	 * 
	 * 
	 * public void setAuthor_email(String author_email) { this.author_email =
	 * author_email; }
	 * 
	 * 
	 * 
	 * public String getAuthor_address() { return author_address; }
	 * 
	 * 
	 * 
	 * public void setAuthor_address(String author_address) { this.author_address =
	 * author_address; }
	 * 
	 * 
	 * 
	 * public String getAuthor_telephone() { return author_telephone; }
	 * 
	 * 
	 * 
	 * public void setAuthor_telephone(String author_telephone) {
	 * this.author_telephone = author_telephone; }
	 * 
	 * 
	 * 
	 * public String getLang_name() { return lang_name; }
	 * 
	 * 
	 * 
	 * public void setLang_name(String lang_name) { this.lang_name = lang_name; }
	 * 
	 * 
	 * 
	 * public String getCopyright_file_loc() { return copyright_file_loc; }
	 * 
	 * 
	 * 
	 * public void setCopyright_file_loc(String copyright_file_loc) {
	 * this.copyright_file_loc = copyright_file_loc; }
	 * 
	 * 
	 * 
	 * public String getDisclaimer_file_loc() { return disclaimer_file_loc; }
	 * 
	 * 
	 * 
	 * public void setDisclaimer_file_loc(String disclaimer_file_loc) {
	 * this.disclaimer_file_loc = disclaimer_file_loc; }
	 * 
	 * 
	 * 
	 * public String getStatus_discription() { return status_discription; }
	 * 
	 * 
	 * 
	 * public void setStatus_discription(String status_discription) {
	 * this.status_discription = status_discription; }
	 * 
	 * 
	 * 
	 * public Copyright getCopyright() { return copyright; }
	 * 
	 * 
	 * 
	 * public void setCopyright(Copyright copyright) { this.copyright = copyright; }
	 * 
	 * 
	 * 
	 * public Disclaimer getDisclaimer() { return disclaimer; }
	 * 
	 * 
	 * 
	 * public void setDisclaimer(Disclaimer disclaimer) { this.disclaimer =
	 * disclaimer; }
	 * 
	 * 
	 * 
	 * public Languages getLanguages() { return languages; }
	 * 
	 * 
	 * 
	 * public void setLanguages(Languages languages) { this.languages = languages; }
	 * 
	 * 
	 * 
	 * public ArticlePubStatus getArticlePubStatus() { return articlePubStatus; }
	 * 
	 * 
	 * 
	 * public void setArticlePubStatus(ArticlePubStatus articlePubStatus) {
	 * this.articlePubStatus = articlePubStatus; }
	 * 
	 * 
	 * 
	 * public Author getAuthor() { return author; }
	 * 
	 * 
	 * 
	 * public void setAuthor(Author author) { this.author = author; }
	 * 
	 */
}
