

import static java.io.File.listRoots;   // OK
import static javax.swing.WindowConstants.*; // OK
import static java.io.File.createTempFile; // violation should be before javax
import static com.puppycrawl.tools.checkstyle;  // OK

public class SomeClass { }
        