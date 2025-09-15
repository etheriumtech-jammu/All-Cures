package util;

import java.time.*;

public final class TimeUtil {
    private TimeUtil() {}

    public static LocalDateTime epochDoubleToUtc(Double epochSecondsNullable) {
        if (epochSecondsNullable == null) return null;
        double epochSeconds = epochSecondsNullable;
        long secs = (long) epochSeconds;
        long nanos = (long) Math.round((epochSeconds - secs) * 1_000_000_000L);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(secs, nanos), ZoneOffset.UTC);
    }
}
