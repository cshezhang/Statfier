
import java.io.FileInputStream;

public class Foo {

    private FileInputStream _s = new FileInputStream("file");

    public int skip(int n) throws IOException {
        return _s.skip(n);
   }
}
        