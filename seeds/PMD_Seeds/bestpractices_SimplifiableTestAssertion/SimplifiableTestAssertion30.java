
            import org.junit.jupiter.api.Test;

            import static org.junit.jupiter.api.Assertions.assertTrue;

            public class Foo {

                Object a, b;

                @Test
                public void test1() {
                    assertTrue(a.equals(b));
                }
            }
            