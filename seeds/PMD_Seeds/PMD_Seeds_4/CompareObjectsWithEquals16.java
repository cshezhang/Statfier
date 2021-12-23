
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class ClassWithFields {
    private Object a;
    private Object b;

    public boolean equals(Object o) {
        if (this == o) { } // should be allowed, since this is a often used pattern in Object::equals.
        // comparing class instances is ok
        if (o.getClass() == a.getClass()) { }
        if (this.getClass() == this.a.getClass()) { }
        if (Object.class == a.getClass()) { }
    }

    void test(Object o) {
        if (this == o) { } // this is not allowed here, only in equals
    }
}
        