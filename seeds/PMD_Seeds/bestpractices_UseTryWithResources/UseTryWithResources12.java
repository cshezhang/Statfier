
import java.io.InputStream;
import java.io.IOException;

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
        