package model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String type;

    @Column(name = "event_at", nullable = false, columnDefinition = "datetime(3)")
    private LocalDateTime eventAt;

    @Column(length = 64)
    private String room;

    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "meeting_id", length = 64)
    private String meetingId;

    @Column(name = "joined_at", columnDefinition = "datetime(3)")
    private LocalDateTime joinedAt;

    @Column(name = "will_eject_at", columnDefinition = "datetime(3)")
    private LocalDateTime willEjectAt;

    private Boolean owner;
    private Boolean hasPresence;
    private Boolean canSend;
    private Boolean canReceiveBase;
    private Boolean canAdmin;

    private Double duration;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getEventAt() { return eventAt; }
    public void setEventAt(LocalDateTime eventAt) { this.eventAt = eventAt; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public LocalDateTime getWillEjectAt() { return willEjectAt; }
    public void setWillEjectAt(LocalDateTime willEjectAt) { this.willEjectAt = willEjectAt; }

    public Boolean getOwner() { return owner; }
    public void setOwner(Boolean owner) { this.owner = owner; }

    public Boolean getHasPresence() { return hasPresence; }
    public void setHasPresence(Boolean hasPresence) { this.hasPresence = hasPresence; }

    public Boolean getCanSend() { return canSend; }
    public void setCanSend(Boolean canSend) { this.canSend = canSend; }

    public Boolean getCanReceiveBase() { return canReceiveBase; }
    public void setCanReceiveBase(Boolean canReceiveBase) { this.canReceiveBase = canReceiveBase; }

    public Boolean getCanAdmin() { return canAdmin; }
    public void setCanAdmin(Boolean canAdmin) { this.canAdmin = canAdmin; }

    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }
}
