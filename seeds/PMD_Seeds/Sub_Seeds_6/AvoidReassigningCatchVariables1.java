
import java.io.File;
import java.io.IOException;
public class Foo {
    public void foo() {
        try {
            new File("/text.txt");
        } catch (IOException e) {
            Exception t;
            t = new NullPointerException();
        }
    }
}
        