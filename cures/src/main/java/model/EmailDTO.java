package model;

import java.util.Map;

public class EmailDTO {

	private String to;
	private String from;
	private String subject;
	private Map<String, Object> emailTemplateData;
	private String emailtext;

	// Generate Getters and Setters...

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getEmailTemplateData() {
		return emailTemplateData;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setEmailTemplateData(Map<String, Object> emailTemplateData) {
		this.emailTemplateData = emailTemplateData;
	}

	public String getEmailtext() {
		return emailtext;
	}

	public void setEmailtext(String emailtext) {
		this.emailtext = emailtext;
	}

}
