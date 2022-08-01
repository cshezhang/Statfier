
import java.io.InputStream;

public class UnusedLocalVariable {

    public void testSomething() {
        InputStream is = new InputStream();

        try (is) {
            System.out.println("foo!");
        }
    }
}
        