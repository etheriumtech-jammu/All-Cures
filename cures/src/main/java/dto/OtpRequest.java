package dto;

public class OtpRequest {
    private Long mobile;
    private String countryCode;
    private String otp;
    private String firstName;
    private String lastName;
    private String acceptTnC;
    private String acceptPolicy;
    private String lockId;
    private Double amount;
    // ✅ Remember Password / Remember Me
    private Integer rememberPassword;

	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAcceptTnC() {
		return acceptTnC;
	}
	public void setAcceptTnC(String acceptTnC) {
		this.acceptTnC = acceptTnC;
	}
	public String getAcceptPolicy() {
		return acceptPolicy;
	}
	public void setAcceptPolicy(String acceptPolicy) {
		this.acceptPolicy = acceptPolicy;
	}

	public String getLockId() {
		return lockId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	

    public Integer getRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(Integer rememberPassword) {
        this.rememberPassword = rememberPassword;
    }
	
    // getters & setters
    
}
