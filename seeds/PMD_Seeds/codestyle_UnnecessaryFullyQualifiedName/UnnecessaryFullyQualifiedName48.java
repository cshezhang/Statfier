
package net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname;

import static net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname.TestClass.*;

public class SomeClass {

    public void theMethod() {
        someMethod();
        Object someObject = new Object();
        if(someObject instanceof TestClass.SomeInnerClass) {
            System.out.println("");
        }
    }
}
        