package controller;

import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.hibernate.Session;

import dto.WebhookEvent;
import mapper.EventMapper;
import model.Event;
import util.HibernateUtil;

@RestController
public class DailyWebhookController {
private static final Logger log = LoggerFactory.getLogger(DailyWebhookController.class);

// @PostMapping(value = "/daily-webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
// @ResponseStatus(HttpStatus.OK)
  
// public void receive(@RequestBody WebhookEvent event,
//                     @RequestHeader(value = "X-Webhook-Signature", required = false) String sig,
//                     @RequestHeader(value = "X-Webhook-Timestamp", required = false) String ts) {

//     log.info("Received webhook: type={}, ts={}, signature={}", event.type, event.event_ts, sig);

//     Event entity = EventMapper.toEntity(event);

//     Session session = null;
//     Transaction tx = null;
//     try {
//         session = HibernateUtil.buildSessionFactory();
//         tx = session.beginTransaction();

//         session.persist(entity);

//         tx.commit();
//         log.info("Persisted event id={}, type={}", entity.getId(), entity.getType());
//     } catch (Exception e) {
//         if (tx != null) tx.rollback();
//         log.error("Failed to persist webhook event", e);
//         throw e; // propagate -> Spring returns 500
//     } 
//}

  	@GetMapping(value = "/daily-webhook")
	public void receive() {
		System.out.println("hello");
		}
  
  
}
