package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "otp_verification")
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mobileNumber")
    private Long mobileNumber;
    
    private String countryCode;
    private String otp;

    private Integer attempts;
    private Integer isUsed;

    private LocalDateTime expiryTime;
    private LocalDateTime createdTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public Integer getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
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

    
    // getters & setters
}