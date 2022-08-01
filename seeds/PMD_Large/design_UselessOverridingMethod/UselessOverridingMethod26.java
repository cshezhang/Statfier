
package net.sourceforge.pmd.lang.java.rule.design.uselessoverridingmethod;

public class DirectSubclass extends BaseClass {

    @Override
    public void doBaseWithArg(String foo) {
        super.doBaseWithArg(foo.toString());
    }
}
        