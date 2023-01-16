import java.io.Closeable;

public final class InferCloseables {

  private InferCloseables() {}

  public static void close(Closeable closeable) {
    if (closeable != null) {
      InferBuiltins.__set_mem_attribute(closeable);
    }
  }

  public static void closeQuietly(Closeable closeable) {
    close(closeable);
  }
}
