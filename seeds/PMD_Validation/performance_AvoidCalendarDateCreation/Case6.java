package MutatorTestingCases;

import java.util.Calendar;
import java.util.Date;

public class Case6 {

    public final Calendar calendar = Calendar.getInstance();

    public void foo() {
//        Date date = Calendar.getInstance().getTime(); // report a warning here
        Date date = calendar.getInstance().getTime(); // not report warning
    }

}
