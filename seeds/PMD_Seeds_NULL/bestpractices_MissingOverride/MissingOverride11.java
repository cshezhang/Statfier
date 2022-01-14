
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

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


    public Object getSomething() {
        return null;
    }
}
        