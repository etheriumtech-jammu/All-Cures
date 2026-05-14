package dto;

public interface UserEventsDto {

    /**
     * Record an event in a deduplicated manner.
     *
     * If clientEventId is non-null and an event with that event_id exists -> skip.
     * Otherwise, if a recent event of same (guid, articleId, eventType, source) exists within dedupeWindowSeconds -> skip.
     * Otherwise insert and return true.
     *
     * @param eventId optional client-supplied idempotency id (UUID)
     * @param guid visitor guid cookie
     * @param eventType event type, e.g. 'article_view'
     * @param articleId optional article id
     * @param source 'server' or 'client'
     * @param payload JSON string or null
     * @param userAgent user agent string
     * @param dedupeWindowSeconds window in seconds to treat events as duplicates (e.g. 5)
     * @return true if inserted, false if deduped/skipped
     */
    boolean recordEvent(String eventId,
                        String guid,
                        String eventType,
                        Long articleId,
                        Integer disease_cond_id,
                        String source,
                        String payload,
                        String userAgent,
                        int dedupeWindowSeconds);
}
