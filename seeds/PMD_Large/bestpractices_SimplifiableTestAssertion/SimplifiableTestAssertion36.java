
            import org.junit.Test;

            import static org.junit.Assert.assertEquals;

            public class Foo {

                @Test
                public void test() {
                    assertEquals(methodWithBooleanParam(Boolean.TRUE), "a String value", "they should be equal!");
                }

                public String methodWithBooleanParam(Boolean param) {
                    return "a String value";
                }
            }
            