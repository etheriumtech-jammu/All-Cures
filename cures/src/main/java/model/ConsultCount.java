package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.LocalDateTime;

import javax.persistence.Column;

@Entity
public class ConsultCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer userID;
    
    @Column(nullable = false)
    private Integer count;
  
    @Column(name = "updated_at")  
    private LocalDateTime updatedAt;

    // Constructors
    public ConsultCount(Integer userID, Integer count,LocalDateTime updatedAt) {
        this.userID = userID;
        this.count = count;
        this.updatedAt=updatedAt;
    }

    public ConsultCount(Long id, Integer userID, Integer count, LocalDateTime updatedAt) {
        this.id = id;
        this.userID = userID;
        this.count = count;
        this.updatedAt=updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    	public Integer getUserId() {
        return userID;
    }

    public void setUserId(Integer userID) {
        this.userID = userID;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
}
