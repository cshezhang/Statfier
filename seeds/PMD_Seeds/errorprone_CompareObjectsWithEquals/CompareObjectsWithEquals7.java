
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class CompareObjectsWithEqualsSample {
    void array(int[] a, String[] b) {
        if (a[1] == b[1]) {} // int == String - this comparison doesn't make sense (and doesn't compile...)
    }
    void array2(int[] c, int[] d) {
        if (c[1] == d[1]) {}
    }
    void array3(String[] a, String[] b) {
        if (a[1] == b[1]) {} // not ok
    }
}
        