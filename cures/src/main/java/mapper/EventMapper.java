package mapper;

import dto.WebhookEvent;
import model.Event;
import util.TimeUtil;

public class EventMapper {
    public static Event toEntity(WebhookEvent dto) {
        Event e = new Event();

        e.setType(dto.type);
        // Prefer payload.start_ts if present for meeting.started; else event_ts
        if (dto.payload != null && dto.payload.start_ts != null) {
            e.setEventAt(TimeUtil.epochDoubleToUtc(dto.payload.start_ts));
        } else {
            e.setEventAt(TimeUtil.epochDoubleToUtc(dto.event_ts));
        }

        if (dto.payload != null) {
            e.setRoom(dto.payload.room);
            e.setSessionId(dto.payload.session_id);
            e.setMeetingId(dto.payload.meeting_id);

            e.setJoinedAt(TimeUtil.epochDoubleToUtc(dto.payload.joined_at));
            e.setWillEjectAt(TimeUtil.epochDoubleToUtc(dto.payload.will_eject_at));

            e.setOwner(dto.payload.owner);

            if (dto.payload.permissions != null) {
                e.setHasPresence(dto.payload.permissions.hasPresence);
                e.setCanSend(dto.payload.permissions.canSend);
                e.setCanReceiveBase(
                    dto.payload.permissions.canReceive != null
                        ? dto.payload.permissions.canReceive.base
                        : null
                );
                e.setCanAdmin(dto.payload.permissions.canAdmin);
            }

            e.setDuration(dto.payload.duration);
        }

        return e;
    }
}
