
import java.io.*;
public class Foo {
    public Runnable bar() {
        InputStream is = new FileInputStream("text.txt");
        return () -> {
            try {
                int d = is.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
        