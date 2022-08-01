
import java.io.IOException;

public class Bug {
    void test() throws IOException {
        try {
            // do something
        } catch (final IOException e) {
            throw new IOException("b") {
                {
                    initCause(e);
                }
            };
        }
    }
}
        