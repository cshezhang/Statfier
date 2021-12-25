
package net.sourceforge.pmd.lang.java.rule.design.uselessoverridingmethod;

public class DirectSubclass2 extends DirectSubclass {
    @Override
    public void doBase() { // it's already public in DirectSubclass
        super.doBase();
    }
}
        