package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMeetingResponse {

	@JsonProperty("url")
	private String url;

	public String getUrl() {
	    return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
	    this.url = url;
	}


    @Override
    public String toString() {
        return "CreateMeetingResponse{" +
                "url='" + url + '\'' +
                '}';
    }
}
