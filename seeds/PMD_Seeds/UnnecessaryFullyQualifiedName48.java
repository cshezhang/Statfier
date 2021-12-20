
package iter0;

import static net.sourceforge.pmd.lang.java.rule.codestyle.unnecessaryfullyqualifiedname.TestClass.*;

public class SomeClass {

    public void theMethod() {
        someMethod();
        java.lang.Object someObject = new java.lang.Object();
        if(someObject instanceof TestClass.SomeInnerClass) {
            System.out.println("");
        }
    }
}
        