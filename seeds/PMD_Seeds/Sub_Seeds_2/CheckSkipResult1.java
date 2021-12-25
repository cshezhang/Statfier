
import java.io.FileInputStream;

public class Foo {

    private FileInputStream _s = new FileInputStream("file");

    public void skip(int n) throws IOException {
        (_s.skip(n));
    }
}
        