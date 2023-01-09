
package net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports;

// star import is important here for the test case!!
import java.util.*;

/**
 * Note: In order for this test case to work, the class "Issue2016" must also be compiled and available
 * on the auxclasspath.
 */
public class Issue2016 {
    public void testFunction() {
        Objects.toString(null);
    }
}
        