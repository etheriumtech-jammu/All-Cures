package service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import model.CreateMeetingResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailyCoService {

    private String dailyCoApiKey = "1e0c217a117ef0f076434b616c12ca6b9705cd92e9b24e5ce677df418332362e";
    private final String DAILY_CO_API_BASE_URL = "https://api.daily.co/v1";
    private final RestTemplate restTemplate;

    public DailyCoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createMeeting() {
        String url = DAILY_CO_API_BASE_URL + "/rooms";

        // Set up request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(dailyCoApiKey);

        // Set up the request entity (body, headers, etc.)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Make the API request
        ResponseEntity<CreateMeetingResponse> responseEntity = restTemplate.exchange(
            url, HttpMethod.POST, requestEntity, CreateMeetingResponse.class);

        // Log the response body and status code for analysis
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response Body: " + responseEntity.getBody());

        // Process the response
        if (responseEntity.getStatusCodeValue() == 200) {
            CreateMeetingResponse meetingResponse = responseEntity.getBody();
            if (meetingResponse != null && meetingResponse.getUrl() != null) {
                return meetingResponse.getUrl();
            } else {
                // Handle errors or return a default value
                return "Error creating a meeting - Meeting response or URL is null";
            }
        } else {
            // Handle errors or return a default value
            return "Error creating a meeting - Status code: " + responseEntity.getStatusCodeValue();
        }

    }
}
