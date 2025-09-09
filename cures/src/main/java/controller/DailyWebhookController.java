package controller;

import model.WebhookEvent;
import util.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DailyWebhookController {
private static final Logger log = LoggerFactory.getLogger(DailyWebhookController.class);

/** The webhook POST URL you give to your provider (e.g., Daily) */
@PostMapping(value = "/daily-webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(HttpStatus.OK)
public void receive(@RequestBody WebhookEvent event,
                   @RequestHeader(value = "X-Webhook-Signature", required = false) String sig,
                   @RequestHeader(value = "X-Webhook-Timestamp", required = false) String ts) {
 // For now we just log + store the event; add signature checks here if needed.
 log.info("Received webhook: type={}, ts={}, signature={}", event.type, event.event_ts, sig);
 EventStore.add(event);
}

/** Convenience endpoint to see what was received */
@GetMapping("/events")
public Object list() {
 return EventStore.all();
}

/** Optional: clear events */
@DeleteMapping("/events")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void clear() {
 EventStore.clear();
}
}
