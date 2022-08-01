
package net.sourceforge.pmd.lang.java.rule.errorprone.useequalstocomparestrings;

public class UseEqualsToCompareStringsSample {
    void bar(String x) {
        if (x != "hello") {}
    }
}
        