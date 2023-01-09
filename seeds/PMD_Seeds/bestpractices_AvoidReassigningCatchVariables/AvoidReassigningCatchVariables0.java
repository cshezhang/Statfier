
import java.io.File;
import java.io.IOException;
public class Foo {
    public void foo() {
        try {
            new File("/text.txt");
        } catch (IOException e) {
            e = new NullPointerException();
        }
    }
}
        