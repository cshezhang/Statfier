
package net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports;

import static net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports.HelloMore.*; // flagged as unused

public class Test {
    public static void main(String... args) {
        sayHello();
    }
}
        