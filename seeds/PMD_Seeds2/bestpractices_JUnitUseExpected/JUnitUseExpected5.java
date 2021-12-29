
import java.lang.Thread;
public class Foo {
    public void foo() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        try {
            r.resolve("PMD");
            fail("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {

        }
    }
}
        