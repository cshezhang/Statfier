import java.io.IOException;
import java.io.InputStream;

public class UseTryWithResources {
  public void read(InputStream is, boolean close) throws IOException {
    try {
      is.read();
    } finally {
      if (close) {
        is.close();
      }
    }
  }
}

