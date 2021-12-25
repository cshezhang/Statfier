
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class CompareObjectsWithEqualsSample {
    boolean bar(int a, int b) {
        return a == b;
    }
    void bar(Integer i, Character c) {
        int l = i;
        if (1 == i) { }
        if (i == Integer.MAX_VALUE) { } // ok
        if (c == Character.MAX_VALUE) { } // ok
    }
}
        