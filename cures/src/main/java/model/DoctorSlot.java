package model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "doctor_slots",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "start_datetime"}))
public class DoctorSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "start_datetime", columnDefinition = "DATETIME(0)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDatetime;
    @Column(name = "end_datetime", columnDefinition = "DATETIME(0)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDatetime;

    @Column(name = "is_booked")
    private Boolean isBooked = false;
    // Getters and Setters
    
    @Column(name = "hold_until", columnDefinition = "DATETIME(0)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime hold_until;
    public Long getSlotId() {
		return slotId;
	}		
    
    public Integer getDoctorId() {
		return doctorId;
	}
    
    	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
    
    	public LocalDateTime getStartDatetime() {
		return startDatetime;
	}
    		
		public void setStartDatetime(LocalDateTime startDatetime) {
		this.startDatetime = startDatetime;
	}
	
		public LocalDateTime getEndDatetime() {
		return endDatetime;
	}
	
		public void setEndDatetime(LocalDateTime endDatetime) {
		this.endDatetime = endDatetime;
	}
	
		public Boolean getIsBooked() {
		return isBooked;
	}
	
		public void setIsBooked(Boolean isBooked) {
		this.isBooked = isBooked;
	}
    	
		public LocalDateTime getHold_until() {
		return hold_until;
	}
		
		public void setHold_until(LocalDateTime hold_until) {
		this.hold_until = hold_until;
	}
}