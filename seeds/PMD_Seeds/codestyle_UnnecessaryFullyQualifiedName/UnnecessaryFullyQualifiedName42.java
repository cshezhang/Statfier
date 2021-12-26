
package net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname;

public class FQNTest {
    public void foo() {
        new net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname.subpackage.MyClass(); // no violation
        new net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname.TestClass(); // violation
    }
}
        