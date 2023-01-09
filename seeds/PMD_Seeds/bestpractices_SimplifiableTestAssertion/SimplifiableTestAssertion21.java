
            import org.junit.Test;
            import static org.junit.Assert.assertFalse;
            public class Foo {
                @Test
                void testBar() {
                    boolean bar;
                    assertFalse(!bar);
                }
            }
            