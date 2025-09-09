package util;

import model.WebhookEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventStore {
private static final List<WebhookEvent> EVENTS = new CopyOnWriteArrayList<>();
public static void add(WebhookEvent e) { EVENTS.add(e); }
public static List<WebhookEvent> all() { return EVENTS; }
public static void clear() { EVENTS.clear(); }
}
