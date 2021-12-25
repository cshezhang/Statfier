
package net.sourceforge.pmd.lang.java.rule.errorprone.useequalstocomparestrings;

public class ClassWithStringFields {
    private String string1 = "a";
    private String string2 = "a";

    public void bar() {
        if (string1 == string2) { }  // violation
        if (string1 == this.string2) { } // violation
        if (this.string1 == string2) { } // violation
        if (this.string1 == this.string2) { } // violation

        if (string1 != string2) { } // violation
        if (string1 != this.string2) { } // violation
        if (this.string1 != string2) { } // violation
        if (this.string1 != this.string2) { } // violation

        if (string1.equals(string2)) { } // ok
        if (this.string1.equals(string2)) { } // ok
        if (string1.equals(this.string2)) { } // ok
        if (this.string1.equals(this.string2)) { } // ok
    }
}
        