
import org.testng.annotations.Test;

public class PercentageTest {
    @Test(successPercentage = 80, invocationCount = 10)  // Noncompliant. The test is allowed to fail 2 times.
    public void flakyTest() {
    }
}
