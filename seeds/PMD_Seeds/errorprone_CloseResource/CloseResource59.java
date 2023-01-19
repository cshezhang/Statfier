import com.google.common.io.Closeables;
import com.google.common.io.Flushables;
import java.io.*;

public class Foo {
  private static void flushAndCloseOutStream(OutputStream stream) throws IOException {
    if (stream != null) {
      Flushables.flush(stream, false);
    }
    Closeables.close(stream, false);
  }
}

