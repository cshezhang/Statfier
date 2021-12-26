
package net.sourceforge.pmd;

public class FinalFieldCouldBeStaticTest {

    public void foo() {
        final Test t = new Test() {
            // the rule was triggered for "foo" - which could be indeed a final static String constant
            private final byte[] b = "foo".getBytes();

            @Override
            public byte[] bar() {
                return b;
            }
        };
        t.bar();
    }
}

interface Test {
    byte[] bar();
}
        