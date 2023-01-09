
import org.joda.time.DateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Foo {
    void foo() {
        DateTime nowDT1 = new DateTime(GregorianCalendar.getInstance());
        DateTime nowDT2 = new DateTime(Calendar.getInstance());
    }
}
        