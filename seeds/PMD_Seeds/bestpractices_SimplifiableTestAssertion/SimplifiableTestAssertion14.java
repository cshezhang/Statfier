
            import org.junit.Test;
            public class Foo {
                Object a,b;
                @Test
                public void test1() {
                    // unresolved methods
                    assertTrue(a==b);
                    assertEquals(a, null);
                }
            }
            