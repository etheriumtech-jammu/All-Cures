package model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "RotationTracker")  // Ensure this matches your database table name
public class RotationTracker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lastIndex")  // Ensure this column exists in the DB
    private Integer lastIndex;


    @Column(name = "updated_at")  // Ensure this column exists in the DB
    private LocalDateTime updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer i) {
		this.id = i;
	}

	public Integer getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(Integer lastIndex) {
		this.lastIndex = lastIndex;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	
    
    // Getters and Setters
}
