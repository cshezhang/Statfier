
import org.junit.*;
public class DigitalAssetManagerTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void throwsIllegalArgumentExceptionIfIconIsNull() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Icon is null, not a file, or doesn't exist.");
        new DigitalAssetManager(null, null);
    }
}
        