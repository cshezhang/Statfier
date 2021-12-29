
import java.io.*;
public class Foo {
    public void bar() {
        InputStream in = null;
        try {
            in = new FileInputStream("test");
        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
        