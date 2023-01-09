
import java.time.ZonedDateTime;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Foo {
    public Date convertToDate(Calendar source) {
        return source.getTime(); // ok
    }

    public Long convertToLong(Calendar source) {
        return source.getTimeInMillis(); // ok
    }

    public ZonedDateTime calendarToZonedDateTime(Calendar source) {
        if (source instanceof GregorianCalendar) {
            return ((GregorianCalendar) source).toZonedDateTime();
        }
        else {
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(source.getTimeInMillis()), // ok
                    source.getTimeZone().toZoneId());
        }
    }
}
        