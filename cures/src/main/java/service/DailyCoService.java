package service;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import model.Appointment;
import model.CreateMeetingResponse;
import util.HibernateUtil;
import util.PaymentUtil;

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

    private String dailyCoApiKey = "cd370e4acc4f13598df4c87fcab72a2b4257c81a9e8bec9926481129d6d6f99d";
    private final String DAILY_CO_API_BASE_URL = "https://api.daily.co/v1";
    private final RestTemplate restTemplate;

    public DailyCoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createMeeting(HttpServletRequest request) {
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
            Map<String, String> hs = PaymentUtil.decryptResponse(request);
            String orderId = hs.get("order_id");
            String meetingLink=meetingResponse.getUrl();
            if (meetingResponse != null && meetingResponse.getUrl() != null) {
            	Session session = HibernateUtil.buildSessionFactory();
                Transaction transaction = null;

                try {
                    transaction = session.beginTransaction();

                    // Step 1: Get appointment ID
                    Query query = session.createQuery(
                        "SELECT p.appointmentID FROM PaymentGatewayTransaction p WHERE p.orderID = :orderId"
                    );
                    query.setParameter("orderId", orderId);
                    Integer appointmentId = (Integer) query.uniqueResult();

                    if (appointmentId != null) {
                        // Step 2: Load Appointment and update meeting link
                        Appointment appointment = session.get(Appointment.class, appointmentId);
                        if (appointment != null) {
                            appointment.setMeetingLink(meetingLink); // Assuming such a setter exists
                            session.merge(appointment);

                        }
                    }

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) transaction.rollback();
                    e.printStackTrace();
                } finally {
                    session.close();
                }
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
