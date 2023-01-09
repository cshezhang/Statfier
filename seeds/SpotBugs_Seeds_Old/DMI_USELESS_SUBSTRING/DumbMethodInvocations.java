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

    @ExpectWarning("DMI_USELESS_SUBSTRING")
    String f(String s) {
        return s.substring(0);
    }

    
}
