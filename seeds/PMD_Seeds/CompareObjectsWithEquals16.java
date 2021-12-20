
package iter0;

public class ClassWithFields {
    private java.lang.Object a;
    private java.lang.Object b;

    public boolean equals(java.lang.Object o) {
        if (this == o) { } // should be allowed, since this is a often used pattern in Object::equals.
        // comparing class instances is ok
        if (o.getClass() == a.getClass()) { }
        if (this.getClass() == this.a.getClass()) { }
        if (java.lang.Object.class == a.getClass()) { }
    }

    void test(java.lang.Object o) {
        if (this == o) { } // this is not allowed here, only in equals
    }
}
        