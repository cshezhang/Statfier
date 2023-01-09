
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

interface CloneableInterface extends Cloneable {
    // nothing to report
    Foo clone() throws CloneNotSupportedException;
}
        