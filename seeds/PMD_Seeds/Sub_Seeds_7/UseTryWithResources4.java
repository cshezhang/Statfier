
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
            myClose(in);
        }

        // this block doesn't trigger the rule because of the custom close methods property
        try {
            in = openInputStream();
            int i = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
        