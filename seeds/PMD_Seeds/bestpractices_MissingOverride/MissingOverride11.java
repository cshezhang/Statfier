
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

public enum EnumWithAnonClass {
    Foo {
        // missing
        public String toString() {
            return super.toString();
        }

        // missing
        public String getSomething() {
            return null;
        }
    };

    public Object getSomething() {
        return null;
    }
}
        