package dto;

public class WebhookEvent {
    public String type;
    public double event_ts; // seconds since epoch (may be fractional)
    public Payload payload;

    public static class Payload {
        public String room;
        public String session_id;
        public Double joined_at;       // may be null
        public Double will_eject_at;   // may be null
        public Boolean owner;
        public Permissions permissions; // may be null
        public Double duration;        // on participant.left
        public String meeting_id;      // on meeting.started
        public Double start_ts;        // on meeting.started
    }

    public static class Permissions {
        public Boolean hasPresence;
        public Boolean canSend;
        public CanReceive canReceive;
        public Boolean canAdmin;
    }

    public static class CanReceive {
        public Boolean base;
    }
}
