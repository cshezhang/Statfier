
            import junit.framework.TestCase;
            public class Foo extends TestCase {
                Object a,b;
                public void test1() {
                    assertFalse(!a.equals(b));
                }
            }
            