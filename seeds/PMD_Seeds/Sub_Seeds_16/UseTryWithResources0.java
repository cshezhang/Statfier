
import java.io.*;

public class TryWithResources {
    public void run() {
        InputStream in = null;
        try {
            in = openInputStream();
            int i = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
                // ignored
            }
        }

        // better use try-with-resources
        try (InputStream in2 = openInputStream()) {
            int i = in2.read();
        }
    }
}
        