
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Foo {
    void foo() {
        Date now = Calendar.getInstance().getTime();
        setDate(Calendar.getInstance().getTime());
        setDate(GregorianCalendar.getInstance().getTime());

        Calendar cal = Calendar.getInstance();
        Date now2 = cal.getTime();
    }
    private void setDate(Date when){
    }
}
        