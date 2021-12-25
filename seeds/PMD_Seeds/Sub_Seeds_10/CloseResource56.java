
import java.io.*;
public class Foo {
    public void bar() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(new byte[10]);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        int c = ois.read();
        bos.close();
    }
}
        