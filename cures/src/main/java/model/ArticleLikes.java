package model;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "article_likes")
public class ArticleLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id")
    private Integer articleID;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getArticleID() {
		return articleID;
	}

	public void setArticleID(Integer articleID) {
		this.articleID = articleID;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	private int likes;

    private int dislikes;

    @Column(name = "CreatedDate")
    private Timestamp CreatedDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp LastUpdatedDate;

    @Column(name = "Status")
    private int Status;

    @Column(name = "CreatedBy")
    private int CreatedBy;

    @Column(name = "UpdatedBy")
    private int UpdatedBy;
    // Getters and setters

	public Timestamp getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		CreatedDate = createdDate;
	}

	public Timestamp getLastUpdatedDate() {
		return LastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		LastUpdatedDate = lastUpdatedDate;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}

	public int getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}
}
