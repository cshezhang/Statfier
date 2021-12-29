
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.metrics.Metric;
import net.sourceforge.pmd.lang.metrics.MetricKey;

public enum EnumWithInterfaces implements MetricKey<ASTAnyTypeDeclaration> {
    Foo {
        public Metric<ASTAnyTypeDeclaration> getCalculator() {
            return null;
        }
    };


    public Metric<ASTAnyTypeDeclaration> getCalculator() {
        return null;
    }
}
        