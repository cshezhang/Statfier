
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FooTest {

    @Mock
    private Bar bar;

    @Test
    public void testFooCallsBar() {
        Foo foo = new Foo(bar);
        foo.doTask();

        Mockito.verify(bar, Mockito.times(1)).actuallyDoTask();
    }
}
        