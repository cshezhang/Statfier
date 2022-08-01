



import java.io.Closeable;

public class Utils {

  public static void closeQuietly(Closeable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (Exception ex) {
    }
  }
}
