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

class DumbMethodInvocations {

    @ExpectWarning("DMI_LONG_BITS_TO_DOUBLE_INVOKED_ON_INT")
    double convertToDouble(int i) {
        return Double.longBitsToDouble(i);
    }

}
