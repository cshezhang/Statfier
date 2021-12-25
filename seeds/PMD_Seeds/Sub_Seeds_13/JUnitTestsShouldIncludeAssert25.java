
import org.junit.Test;

class Style {
    // This used to trigger the JUnitTestsShouldIncludeWarning rule
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void moveOutOfBoundsFrom() {
        doSomething();
    }
}
        