
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class CompareObjectsWithEqualsSample {
    boolean bar(String a, String b) {
        return a.charAt(0) == b.charAt(0);
    }
}
        