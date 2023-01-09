
import org.junit.Assert;
import org.junit.Test;

public class MyTest {
    @Test
    public void testSomething() {
        Object check = new Object() { // false positive
            @Override
            public boolean equals(Object o) { return false; }
        };
        Assert.assertTrue(check.equals(null));
    }
}
        