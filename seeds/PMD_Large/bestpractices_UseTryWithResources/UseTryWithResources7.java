
import java.io.*;

public class TryWithResources {
    public void run() {
        InputStream in = null, in2 = null;
        try {
            in = openInputStream();
            in2 = openInputStream();
            int i = in.read(), j = in2.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            myClose(in, in2);
        }
    }
}
        