
package net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports;

import static net.sourceforge.pmd.lang.java.rule.bestpractices.unusedimports.ClassWithConstants.*;

public class ClassWithImport {

    public static void main(String[] args) {
        System.out.println("List 1: " + LIST1);
        System.out.println("List 2: " + LIST2);
    }
}
        