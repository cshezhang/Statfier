
import java.util.Calendar;

public class Foo {
    void foo() {
        long time = Calendar.getInstance().getTimeInMillis();
        String timeStr = Long.toString(Calendar.getInstance().getTimeInMillis());
    }
}
        