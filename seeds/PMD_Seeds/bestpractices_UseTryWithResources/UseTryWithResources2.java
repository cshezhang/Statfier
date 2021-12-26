
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
            closeQuietly(in);
        }
    }
}
        