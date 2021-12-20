
package iter0;

public class ClassWithFields {
    private java.lang.Object a;
    private java.lang.Object b;

    boolean test1() {
        return  a == b // violation
             && this.a == b // violation
             && a != this.b // violation
             && this.a == this.b // violation
             && a.equals(b) // ok
             && this.a.equals(this.b); // ok
    }
}
        