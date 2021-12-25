
import java.io.FileInputStream;

public class Foo {

    private FileInputStream _s = new FileInputStream("file");

    public void skip(int n) throws IOException {
        while (n != 0) {
            long skipped = _s.skip(n);
            if (skipped == 0)
                throw new EOFException();
            n -= skipped;
        }
    }
}
        