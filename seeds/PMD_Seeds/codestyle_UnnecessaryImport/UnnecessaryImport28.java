
package net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports;

import static net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports.ClassWithStringConstants.*;

public class ClassWithImport {

    public static void main(String[] args) {
        if (CONST1.equals("a")) {
            System.out.println("CONST1 is a");
        }
    }
}
        