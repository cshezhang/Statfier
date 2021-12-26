
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.*;
import junit.framework.TestCase;
public class FooTest extends TestCase {
    public void test1() {
        MatcherAssert.assertThat("doesn't matter", null, Matchers.nullValue());
    }
    public void test2() {
        assertThat("doesn't matter", null, Matchers.nullValue());
    }
}
        