
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class Foo {

    public int foo() {
        int a = 0;
        while (a > 10) {
            try (Reader r = new StringReader("")) {
                r.read();
            } catch (IOException e) {
                a = -1; // used in finally even if break
                break;
            } finally {
                a++;
            }
        }
        return a;
    }

}
        