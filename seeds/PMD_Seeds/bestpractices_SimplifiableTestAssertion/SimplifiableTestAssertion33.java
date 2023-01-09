
            import org.junit.Test;

            import junit.framework.TestCase;

            import static org.junit.Assert.assertEquals;

            public class MyTestCase extends TestCase {

                public void testMyCaseWithAssertEqualsOnBoolean() {
                    Object myVar = true;
                    assertEquals(true, myVar);
                    assertNotEquals(true, myVar);
                }
            }
            