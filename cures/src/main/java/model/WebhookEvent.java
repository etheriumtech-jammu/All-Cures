package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookEvent {
public String type;         // e.g., "meeting.started"
public Long event_ts;       // optional (Unix ms or s)
public Map<String, Object> payload; // arbitrary fields
}
