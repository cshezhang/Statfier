
import org.junit.*;
public class SimpleExpectedExceptionTest {
     @org.junit.Rule
     public ExpectedException thrown = ExpectedException.none();

     @Test
     public void throwsExceptionWithSpecificType() {
         thrown.expect(NullPointerException.class);
         throw new NullPointerException();
     }
 }
        