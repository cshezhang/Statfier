
import java.io.*;
import com.google.common.io.Closeables;
import com.google.common.io.Flushables;

public class Foo {
    private static void flushAndCloseOutStream(OutputStream stream) throws IOException {
        if (stream != null) {
            Flushables.flush(stream, false);
        }
        Closeables.close(stream, false);
    }
}
        