package model;

import javax.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT
    @Column(name = "pres_id")
    private Integer presId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "file_path", length = 1024, nullable = false)
    private String filePath;

    @Column(name = "original_name", length = 255)
    private String originalName;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "uploaded_by", nullable = false)
    private Integer uploadedBy;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "status", length = 32, nullable = false)
    private String status = "ACTIVE";

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AppointmentID", referencedColumnName = "appointment_id", insertable = false, updatable = false)
    @Where(clause = "status = 'ACTIVE'")
    @Fetch(FetchMode.JOIN)  // optional if you usually want to load together
    private Prescription prescription;

    public Prescription() {}

    // getters / setters omitted for brevity — add them or use Lombok

    @PrePersist
    protected void onCreate() {
        if (uploadedAt == null) uploadedAt = LocalDateTime.now();
        if (issuedAt == null) issuedAt = uploadedAt;
    }

    // getters & setters...
    public Integer getPresId() { return presId; }
    public void setPresId(Integer presId) { this.presId = presId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public LocalDateTime getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDateTime followUpDate) { this.followUpDate = followUpDate; }

    public Integer getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Integer uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
