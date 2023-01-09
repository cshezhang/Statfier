
            import org.junit.Test;
            import static org.junit.Assert.*;
            public class Foo {
                Object a,b;
                @Test
                public void test1() {
                    assertTrue(a==b);
                }
            }
            