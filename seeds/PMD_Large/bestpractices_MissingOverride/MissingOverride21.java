
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

public interface CloneableInterfaceOverride extends CloneableInterface {

    // Missing @Override
    CloneableInterface clone() throws CloneNotSupportedException;
}
        