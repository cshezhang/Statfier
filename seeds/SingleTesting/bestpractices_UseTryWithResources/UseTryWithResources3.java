
import java.io.*;

public class TryWithResources {
    public void run() {
        InputStream in1 = null;
        InputStream in2 = null;
        try {
            in1 = openInputStream();
            in2 = openInputStream();
            int x = in1.read();
            int y = in2.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in1 != null) in1.close();
            } catch (IOException ignored) {
                // ignored
            }
            IOUtils.closeQuietly(in2);
        }

        // better use try-with-resources
        try (InputStream in3 = openInputStream(); InputStream in4 = openInputStream()) {
            int x = in3.read();
            int y = in4.read();
        }
    }
}
        