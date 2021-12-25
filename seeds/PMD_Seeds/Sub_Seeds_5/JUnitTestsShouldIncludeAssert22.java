
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FooTest {

    @Mock
    private Bar bar;

    @Test
    public void testFooCallsBar() {
        Foo foo = new Foo(bar);
        foo.doTask();

        verify(bar, times(1)).actuallyDoTask();
    }
}
        