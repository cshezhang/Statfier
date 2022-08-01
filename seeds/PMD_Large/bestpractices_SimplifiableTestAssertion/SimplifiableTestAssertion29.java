
            import org.junit.Test;
            import org.junit.Assert;
            public class Foo {
                Object a,b;
                @Test
                public void test1() {
                    Assert.assertTrue(a.equals(b));
                }
            }
            