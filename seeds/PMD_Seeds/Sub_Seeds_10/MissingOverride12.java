
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.metrics.Metric;
import net.sourceforge.pmd.lang.metrics.MetricKey;

public enum EnumWithInterfaces implements MetricKey<ASTAnyTypeDeclaration> {
    Foo;

    // missing
    // this is determined from the bridge method, but only works if there are no overloads
    public boolean supports(ASTAnyTypeDeclaration node) {
        return false;
    }
}
        