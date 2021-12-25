
package net.sourceforge.pmd.lang.java.rule.errorprone.compareobjectswithequals;

public class ClassWithFields {
    private Object a;
    private Object b;

    boolean test1() {
        return a == null // ok
             && this.a == null; // ok
    }

    void test2(StringBuilder sb) {
        if (sb != null) { } // ok
    }

    public void bar(Integer x, Integer y) {
        if (x == 1) { } // ok
        if (x == y) { } // not ok! only works -128<x<127 dependent on Integer caching
        if (x == "String") { } // not ok
    }
}
        