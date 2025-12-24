package model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID")
    private int appointmentID;

    @Column(name = "DocID")
    private int docID;

    @Column(name = "UserID")
    private int userID;

    @Column(name = "AppointmentDate")
    private Date appointmentDate;

    @Column(name = "StartTime")
    private String startTime;

    @Column(name = "EndTime")
    private String endTime;

    @Column(name = "RequestStatus")
    private int requestStatus;

    @Column(name = "MeetingLink")
    private String meetingLink;

    @Column(name = "IsPaid", nullable = true, columnDefinition = "TINYINT(1) DEFAULT NULL")
    private Boolean isPaid;

    @Column(name = "PaymentStatus")
    private int paymentStatus;

    @Column(name = "FailureID")
    private int failureID;

    @Column(name = "CreatedDate")
    private Timestamp createdDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp lastUpdatedDate;

    @Column(name = "Status")
    private int status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AppointmentID", referencedColumnName = "AppointmentID", insertable = false, updatable = false)
    
    @Fetch(FetchMode.JOIN)
    private Prescription prescription;

    @Transient
    private String doctorName;

    @Transient
    private Integer slotDuration;

    @Transient
    private String imgLoc;

    @Transient
    private String medicineType;

    // Getters and setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getFailureID() {
        return failureID;
    }

    public void setFailureID(int failureID) {
        this.failureID = failureID;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(Integer slotDuration) {
        this.slotDuration = slotDuration;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getImgLoc() {
        return imgLoc;
    }

    public void setImgLoc(String imgLoc) {
        this.imgLoc = imgLoc;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @PrePersist
protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    this.createdDate = now;
}

@PreUpdate
protected void onUpdate() {
    this.lastUpdatedDate = new Timestamp(System.currentTimeMillis());
}

}
