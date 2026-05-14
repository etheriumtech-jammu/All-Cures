package dto;

public class SlotRequest {

	private int doctorId;

	// Example: 2026-05-10T14:30
	private String appointmentDateTime;

	private Long mobile;
	private String countryCode;

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public String getAppointmentDateTime() {
		return appointmentDateTime;
	}

	public void setAppointmentDateTime(String appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}

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
	// getters/setters
}