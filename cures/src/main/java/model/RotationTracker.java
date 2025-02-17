package model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "RotationTracker")  // Ensure this matches your database table name
public class RotationTracker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lastIndex")  // Stores the last index offset
    private Integer lastIndex;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "RotatedDocIds", joinColumns = @JoinColumn(name = "rotation_tracker_id"))
    @Column(name = "doc_id")
    private List<Integer> docIds;  // Stores the rotated Doc IDs

    @Column(name = "updated_at")  
    private LocalDateTime updatedAt;

    // Constructor
    public RotationTracker() {
        this.lastIndex = 0;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public List<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(List<Integer> docIds) {
        this.docIds = docIds;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
