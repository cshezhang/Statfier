
            import org.junit.Test;

            import static org.junit.Assert.assertTrue;

            public class Foo {

                @Test
                public void test() {
                    assertTrue(!Thread.currentThread().getName().equals(event.content));
                    assertTrue(Thread.currentThread().getName().equals(event.content));
                }
            }
            