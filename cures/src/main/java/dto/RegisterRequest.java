package dto;
public class RegisterRequest {

    private Long mobile;
    private String countryCode;
    private String password;
    private String firstName;
    private String lastName;
    private String acceptTnC;
    private String acceptPolicy;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

	public Integer getRememberPassword() {
		return rememberPassword;
	}
	
	public void setRememberPassword(Integer rememberPassword) {
			this.rememberPassword = rememberPassword;
	}
    // getters/setters
    
}
