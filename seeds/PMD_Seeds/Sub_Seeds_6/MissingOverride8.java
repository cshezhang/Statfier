
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.metrics.Metric;
import net.sourceforge.pmd.lang.metrics.MetricKey;

public enum EnumWithInterfaces implements MetricKey<ASTAnyTypeDeclaration> {
    Foo {
        @Override
        public Metric<ASTAnyTypeDeclaration> getCalculator() {
            return null;
        }
    };

    @Override
    public Metric<ASTAnyTypeDeclaration> getCalculator() {
        return null;
    }


    @Override
    public boolean supports(ASTAnyTypeDeclaration node) {
        return false;
    }
}
        