
import java.io.*;
public class Foo {
    public void bar() {
        try (InputStream is = new FileInputStream("text.txt")) {
            ObjectInputStream ois = new ObjectInputStream(is);
            int d = ois.read();
        }
    }
}
        