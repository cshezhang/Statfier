
import java.io.*;

public class Foo {
    public int bar() {
        InputStream inputStream = getInputStreamFromSomewhere();
        if (inputStream != null) {
            try (InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {
                char c = reader.read();
                return c;
            }
        }
        return -1;
    }

    InputStream getInputStreamFromSomewhere() { return null; }
}
        