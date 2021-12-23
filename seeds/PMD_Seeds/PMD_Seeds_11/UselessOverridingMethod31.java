
package net.sourceforge.pmd.lang.java.rule.design.uselessoverridingmethod;

public class UselessOverridingMethodHashCode {
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
        