
package net.sourceforge.pmd.lang.java.rule.codestyle;

public class UnnecessaryFullyQualifiedName {
    public static void main(String[] args) {
        System.out.println(UnnecessaryFullyQualifiedNameTest.ENUM1.A);
    }
}
        