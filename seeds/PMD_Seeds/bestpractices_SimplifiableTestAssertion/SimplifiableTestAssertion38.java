
            import org.junit.Test;

            import static org.junit.Assert.assertFalse;

            public class Foo {
                Object pc1, pc2;
                @Test
                public void test() {
                    assertFalse(pc1 == pc2);
                    assertFalse(pc1.hashCode() == pc2.hashCode());
                }
            }
            