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

    @ExpectWarning("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public void testFile() {
        new File("c:\\test.txt");
    }

    @ExpectWarning("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public void testFile2() {
        new File("c:\\temp", "test.txt");
    }

    @ExpectWarning("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public void testPrintStream() throws IOException {
        new PrintStream("c:\\test.txt", "UTF-8");
    }

    @NoWarning("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public void testPrintStream2() throws IOException {
        new PrintStream("UTF-8", "c:\\test.txt");
    }
    
}
