
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class ClassWithFields {
    private Object a;
    private Object b;

    boolean test1() {
        return  a == b // violation
             && this.a == b // violation
             && a != this.b // violation
             && this.a == this.b // violation
             && a.equals(b) // ok
             && this.a.equals(this.b); // ok
    }
}
        