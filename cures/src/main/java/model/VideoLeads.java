package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VideoLeads {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer userID;
    private Integer docID;
    private Integer Status;
    private String timestamp; // You can use a Date or LocalDateTime for better date handling

    // Constructors, getters, and setters
    public VideoLeads() {}

    public VideoLeads(Integer userID,Integer docID, String timestamp) {
        this.userID = userID;
        this.docID = docID;
        this.timestamp = timestamp;
        
    }

	public Integer getDocID() {
		return docID;
	}

	public void setDocID(Integer docID) {
		this.docID = docID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
