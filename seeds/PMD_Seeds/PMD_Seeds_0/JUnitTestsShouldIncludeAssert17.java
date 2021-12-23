
import org.junit.Test;
public class Foo_Test
{
    /** Tests that doSomething throws an exception if arg is negative */
    @Test(expected = IllegalArgumentException.class)
    public  void testDoSomethingFail()
    {
        new Foo().doSomething(-1);
        // Note - no assert is needed as we expect an exception (which is basically an assert).
    }
}
        