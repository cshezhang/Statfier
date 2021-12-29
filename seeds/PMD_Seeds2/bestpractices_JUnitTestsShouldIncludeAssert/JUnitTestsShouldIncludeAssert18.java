
import org.junit.*;
public class SimpleExpectedExceptionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsExceptionWithSpecificType() {
        throw new NullPointerException(); // No expect! this is a violation
    }

    @Test
    public void throwsIllegalArgumentExceptionIfIconIsNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Icon is null, not a file, or doesn't exist.");
        new DigitalAssetManager(null, null);
    }
}
        