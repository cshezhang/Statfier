
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

public class SubclassWithGenericMethod extends AbstractClass {

    // missing
    public <P, Q> Q generic(P t, Q r) { // generic param names are different from superclass
        return super.generic(t, r);
    }
}
        