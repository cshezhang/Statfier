
            import org.junit.Test;

            import static org.junit.Assert.assertEquals;

            public class Foo {

                @Test
                public void test() {
                    final boolean myVar = true;
                    assertEquals("myVar is true", Boolean.TRUE, myVar);
                    assertEquals("myVar is true", myVar, Boolean.TRUE);
                    assertEquals(Boolean.TRUE, myVar);
                    assertEquals(myVar, Boolean.TRUE);
                    assertEquals("myVar is false", Boolean.FALSE, myVar);
                    assertEquals("myVar is false", myVar, Boolean.FALSE);
                    assertEquals(myVar, Boolean.FALSE);
                    assertEquals(Boolean.FALSE, myVar);
                    assertTrue(myVar);
                }
            }
            