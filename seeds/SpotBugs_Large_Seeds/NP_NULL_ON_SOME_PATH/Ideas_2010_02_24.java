
import java.util.Date;

import org.joda.time.DateTimeConstants;
import org.joda.time.Instant;
import org.joda.time.Interval;

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

public class Ideas_2010_02_24 {

    @ExpectWarning(value = "NP_NULL_ON_SOME_PATH")
    public static void truePositive(boolean b) {
        String str = null;
        for (int i = 0; i < 2; i++) {
            if (b)
                str = new String("Test");

            str.charAt(i); // Error : "Null pointer access: The
            // variable str can only be null at
            // this location"
            str = null;
        }
    }
    
}
