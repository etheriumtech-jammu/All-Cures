package model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class DoctorVideos{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Integer docId;
	private String videoUrl;
	private Integer diseaseCondition;
	private Integer articleId;
	private LocalDateTime lastUsed;
	private LocalDateTime createdDate;
	private LocalDateTime lastUpdatedDate;
	private Integer status;
	private Integer createdBy;
	private Integer updatedBy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public Integer getDiseaseCondition() {
		return diseaseCondition;
	}
	public void setDiseaseCondition(Integer diseaseCondition) {
		this.diseaseCondition = diseaseCondition;
	}
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	public LocalDateTime getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(LocalDateTime lastUsed) {
		this.lastUsed = lastUsed;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	

	
}
