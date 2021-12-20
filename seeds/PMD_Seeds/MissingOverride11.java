
package iter0;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.metrics.Metric;
import net.sourceforge.pmd.lang.metrics.MetricKey;

public enum EnumWithAnonClass {
    Foo {
        // missing
        public String toString() {
            return super.toString();
        }


        // missing
        public String getSomething() {
            return null;
        }
    };


    public java.lang.Object getSomething() {
        return null;
    }
}
        