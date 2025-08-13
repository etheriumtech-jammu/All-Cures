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

     public String createMeeting(HttpServletRequest request, Appointment directAppointment) {
        String url = DAILY_CO_API_BASE_URL + "/rooms";

        // Set up request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(dailyCoApiKey);

        // Set up the request entity (headers only, no body required)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Make the API request
        ResponseEntity<CreateMeetingResponse> responseEntity = restTemplate.exchange(
            url, HttpMethod.POST, requestEntity, CreateMeetingResponse.class
        );

        // Log the response for debugging
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response Body: " + responseEntity.getBody());

        // Check if request succeeded
        if (responseEntity.getStatusCodeValue() == 200) {
            CreateMeetingResponse meetingResponse = responseEntity.getBody();

            if (meetingResponse != null && meetingResponse.getUrl() != null) {
                String meetingLink = meetingResponse.getUrl();

                Session session = HibernateUtil.buildSessionFactory();
                Transaction transaction = null;

                try {
                    transaction = session.beginTransaction();

                    if (request != null) {
                        // ---- CASE 1: Payment flow ----
                        Map<String, String> hs = PaymentUtil.decryptResponse(request);
                        String orderId = hs.get("order_id");

                        Query query = session.createQuery(
                            "SELECT p.appointmentID FROM PaymentGatewayTransaction p WHERE p.orderID = :orderId"
                        );
                        query.setParameter("orderId", orderId);
                        Integer appointmentId = (Integer) query.uniqueResult();

                        if (appointmentId != null) {
                            Appointment appointment = session.get(Appointment.class, appointmentId);
                            if (appointment != null) {
                                appointment.setMeetingLink(meetingLink);
                                session.merge(appointment);
                            }
                        }

                    } else if (directAppointment != null) {
                        // ---- CASE 2: Non-payment flow ----
                        directAppointment.setMeetingLink(meetingLink);
                        session.merge(directAppointment);
                    }

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) transaction.rollback();
                    e.printStackTrace();
                } finally {
                    session.close();
                }

                return meetingLink;
            } else {
                return "Error creating a meeting - Meeting response or URL is null";
            }
        } else {
            return "Error creating a meeting - Status code: " + responseEntity.getStatusCodeValue();
        }
    }

}
