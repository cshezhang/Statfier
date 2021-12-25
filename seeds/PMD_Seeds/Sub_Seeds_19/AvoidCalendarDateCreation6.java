
import java.util.Calendar;
import java.util.Date;

public class Foo {
    public Date onlySet(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        long originalTimestamp = calendar.getTimeInMillis();
        return calendar.getTime();
    }

    public Date onlyAdd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        long originalTimestamp = calendar.getTimeInMillis();
        return calendar.getTime();
    }

    public Date onlyClear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.MILLISECOND);
        long originalTimestamp = calendar.getTimeInMillis();
        return calendar.getTime();
    }
}
        