package model;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "slot_lock")
public class SlotLock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lock_id", unique = true, nullable = false)
	private String lockId;

	@Column(name = "doctor_id", nullable = false)
	private Integer doctorId;

	@Column(name = "appointment_time", nullable = false)
	private LocalDateTime appointmentTime;

	@Column(name = "mobile_number", nullable = false)
	private Long mobileNumber;

	@Column(name = "subscriber_id")
	private Long subscriberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SlotLockStatus status = SlotLockStatus.LOCKED;

	@Column(name = "expiry_time", nullable = false)
	private LocalDateTime expiryTime;

	@Column(name = "created_time", nullable = false, updatable = false)
	private LocalDateTime createdTime;

	@PrePersist
	public void prePersist() {

		this.createdTime = LocalDateTime.now();

		this.expiryTime = LocalDateTime.now().plusMinutes(10);

		if (this.status == null) {
			this.status = SlotLockStatus.LOCKED;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLockId() {
		return lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(LocalDateTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public SlotLockStatus getStatus() {
		return status;
	}

	public void setStatus(SlotLockStatus status) {
		this.status = status;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	// getters setters
}