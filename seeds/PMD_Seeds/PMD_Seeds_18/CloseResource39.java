
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CloseResourceWithExceptions {

    {
        ByteArrayInputStream bis = new ByteArrayInputStream("fooString".getBytes(StandardCharsets.UTF_8));
    }

    public int bar() {
        /*ByteArray*/InputStream buffer = new ByteArrayInputStream();
        try {
            return buffer.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
        