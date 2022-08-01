
import java.lang.Thread;
import org.junit.Test;
public class Foo {
    @Test
    public void foo() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        try {
            r.resolve("PMD");
            fail("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {

        }
    }
}
        