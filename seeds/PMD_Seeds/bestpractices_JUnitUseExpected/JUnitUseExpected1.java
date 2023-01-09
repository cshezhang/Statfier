
import java.lang.Thread;
import junit.framework.TestCase;
public class Foo extends TestCase {
    public void testFoo() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        try {
            r.resolve("PMD");
            fail("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {

        }
    }
}
        