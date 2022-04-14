
package net.sourceforge.pmd.lang.java.rule.errorprone.useequalstocomparestrings;

public class ClassWithStringFields {
    private String string1 = "a";
    private String string2 = "a";

    public void bar() {
        if (string1 == null) { } // ok
        if (this.string1 == null) { } // ok
    }

    public void bar(String param) {
        if (param != null) { } // ok
    }
}
        