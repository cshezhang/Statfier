import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

class DumbMethodInvocations implements Iterator {

    String g(String s) {
        for (int i = 0; i < s.length(); i++)
            if (s.substring(i).hashCode() == 42)
                return s;
        return null;
    }

    @Override
    @ExpectWarning("DMI_CALLING_NEXT_FROM_HASNEXT")
    public boolean hasNext() {
        return next() != null;
    }

    @Override
    @ExpectWarning("IT_NO_SUCH_ELEMENT")
    public Object next() {
        return null;
    }

    @Override
    public void remove() {
    }

    public void falsePositive() {
        Date today = Calendar.getInstance().getTime();
        System.out.println(today);
        today.setDate(16);
        System.out.println(today);
    }

}
