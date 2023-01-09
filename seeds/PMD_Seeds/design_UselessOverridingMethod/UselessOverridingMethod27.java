
package net.sourceforge.pmd.lang.java.rule.design.uselessoverridingmethod.other;

import net.sourceforge.pmd.lang.java.rule.design.uselessoverridingmethod.BaseClass;

public class DirectSubclassInOtherPackage extends BaseClass {

    @Override
    protected void doBase() {
        super.doBase();
    }
}
        