
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Matchers.any; // original problem - this is needed for disambiguation
//import static org.mockito.Matchers.*;

import static org.junit.Assert.*;
import static net.sourceforge.pmd.lang.java.rule.codestyle.DuplicateImportsTest.*;
import static org.junit.Assert.assertTrue; // this import is neeeded for disambiguation - as DuplicateImportsTest
                                           // defines assertTrue with the same signature, too.

public class DuplicateImports {
}
        