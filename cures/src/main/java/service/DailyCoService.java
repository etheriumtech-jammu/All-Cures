package service;

import javax.servlet.http.HttpServletRequest;
import model.Appointment;
import util.HibernateUtil;
import util.PaymentUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DailyCoService {

  private static final String DAILY_API_BASE = "https://api.daily.co/v1";
  private static final Logger log = LoggerFactory.getLogger(DailyCoService.class);
  private final RestTemplate restTemplate; 
  public DailyCoService(RestTemplate restTemplate)
  { this.restTemplate = restTemplate; 
  }

//  @Value("${dailyco.api.key}")
  private String dailyApiKey = "cd370e4acc4f13598df4c87fcab72a2b4257c81a9e8bec9926481129d6d6f99d";
  
  /**
   * Create a room and persist its URL against the appointment.
   * IMPORTANT: We DO NOT mint a meeting token here. Tokens should be minted JIT when the user joins.
   */
  @Transactional
  public String createMeeting(HttpServletRequest request, Appointment directAppointment) {
    // 1) Create room
    final String roomUrl = createRoomUrl();
    final String roomName = extractRoomName(roomUrl);
    if (roomName == null || roomName.isBlank()) {
      throw new IllegalStateException("Unable to extract room name from URL: " + roomUrl);
    }

    // 2) Persist appointment changes (link only)
    persistAppointmentChanges(request, directAppointment, roomUrl);

    // 3) Return room info 
    return roomName;
  }

  // ------------------ Token issuance (JIT on join) ------------------

  /**
   * Public method used by your JoinController to mint a short-lived token JIT.
   *
 @param roomName    Daily room name (e.g., "abc123")
   * @param userId      Your app's user ID (optional but recommended)
   * @param userName    Display name in the call UI (optional)
   * @param isOwner     Whether this participant should join as owner (least-privilege: default false)
   * @param ttlSeconds  Token lifetime (default 600s if null or < 60)
   */
  public String createMeetingToken(String roomName,
                                   String userId,
                                   String userName,
                                   boolean isOwner,
                                   Integer ttlSeconds) {
    Objects.requireNonNull(roomName, "roomName is required");

    final int ttl = (ttlSeconds == null || ttlSeconds < 60) ? 600 : ttlSeconds;
    final long now = Instant.now().getEpochSecond();

    final String url = DAILY_API_BASE + "/meeting-tokens";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(dailyApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

    Map<String, Object> properties = new HashMap<>();
    properties.put("room_name", roomName);
    properties.put("exp", now + ttl);                 // short-lived token
    properties.put("eject_at_token_exp", true);       // auto-eject on expiry (nice for time-boxing)
    properties.put("is_owner", isOwner);

    if (userId != null && !userId.isBlank()) {
      properties.put("user_id", userId);
    }
    if (userName != null && !userName.isBlank()) {
      properties.put("user_name", userName);
    }

    Map<String, Object> body = new HashMap<>();
    body.put("properties", properties);

    try {
      HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
      ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

      if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
        throw new IllegalStateException("Failed to create meeting token. Status=" + res.getStatusCode());
      }
      Object t = res.getBody().get("token");
      if (t == null) {
        throw new IllegalStateException("Token missing in response: " + res.getBody());
      }
      return t.toString();
    } catch (RestClientException e) {
      log.error("Daily token creation failed", e);
      throw new IllegalStateException("Daily token creation failed: " + e.getMessage(), e);
    }
  }


  // ------------------ Room API calls ------------------

  /** Returns the room URL from POST /rooms */
  private String createRoomUrl() {
    final String url = DAILY_API_BASE + "/rooms";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(dailyApiKey);
    headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

    // You can add room config here if you like (e.g., privacy, enable_knocking, owner_only_broadcast, etc.)
    // For bare minimum creation, keep the body empty.
    HttpEntity<Map<String, Object>> req = new HttpEntity<>(new HashMap<>(), headers);

    try {
      ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);
      if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
        throw new IllegalStateException("Failed to create room. Status=" + res.getStatusCode());
      }

      Object urlObj = res.getBody().get("url");
      if (urlObj == null) {
        throw new IllegalStateException("Room created but 'url' missing in response: " + res.getBody());
      }
      return urlObj.toString();
    } catch (RestClientException e) {
      e.printStackTrace();
      throw new IllegalStateException("Daily room creation failed: " + e.getMessage(), e);
    }
  }

  // ------------------ Persistence ------------------
private void persistAppointmentChanges(HttpServletRequest request,
                                         Appointment directAppointment,
                                         String roomUrl) {
	 
	   Session session = null;
	    session=HibernateUtil.buildSessionFactory(); 
    try {
      Transaction tx = session.beginTransaction();
      try {
        if (request != null) {
          // CASE 1: Payment flow
          Map<String, String> hs = PaymentUtil.decryptResponse(request);
          String orderId = hs.get("order_id");

          Integer appointmentId = (Integer) session.createQuery(
              "SELECT p.appointmentID FROM PaymentGatewayTransaction p WHERE p.orderID = :orderId")
              .setParameter("orderId", orderId)
              .uniqueResultOptional()
              .orElse(null);

          if (appointmentId != null) {
            Appointment appt = session.get(Appointment.class, appointmentId);
            if (appt != null) {
              appt.setMeetingLink(roomUrl);
              appt.setPaid(true);
			 appt.setStatus(1);
              session.merge(appt);
            } else {
              log.warn("Appointment not found for id {}", appointmentId);
            }
          } else {
            log.warn("No appointmentId found for orderId {}", orderId);
          }
        } else if (directAppointment != null) {
          // CASE 2: Non-payment flow
          directAppointment.setMeetingLink(roomUrl);
          directAppointment.setPaid(false);
			 directAppointment.setStatus(1);
          session.merge(directAppointment);
        } else {
          log.warn("Neither request nor directAppointment provided; nothing persisted.");
        }
        tx.commit();
      } catch (Exception e) {
        if (tx != null) tx.rollback();
        throw e;
      }
    } catch (Exception e) {
        throw e;
      }
  }

  // ------------------ Utilities ------------------

  private static String extractRoomName(String roomUrl) {
    if (roomUrl == null) return null;
    int slash = roomUrl.lastIndexOf('/');
    return (slash >= 0 && slash < roomUrl.length() - 1)
        ? roomUrl.substring(slash + 1)
        : roomUrl;
  }


  
 
}
