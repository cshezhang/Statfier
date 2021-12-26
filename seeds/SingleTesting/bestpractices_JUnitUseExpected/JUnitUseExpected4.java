
import java.lang.Thread;
import junit.framework.TestCase;
public class Foo extends TestCase {
    public void testFoo() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        try {
            r.resolve("PMD");
            throw new RuntimeException("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {
        // one
        }
        try {
            r.resolve("FOO");
            throw new RuntimeException("Should have thrown an exception also");
        } catch (ClassNotFoundException cnfe) {
        // another
        }
    }
}
        