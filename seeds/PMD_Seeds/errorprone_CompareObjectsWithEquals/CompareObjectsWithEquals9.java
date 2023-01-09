
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

import java.io.File;

public class CompareObjectsWithEqualsSample {
    boolean bar(String b) {
        return new File(b).exists() == false;
    }
}
        