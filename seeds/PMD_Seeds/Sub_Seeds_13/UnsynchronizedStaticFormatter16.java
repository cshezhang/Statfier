
import java.text.SimpleDateFormat;

public class Foo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    static void bar() {
        synchronized(Foo.class) { // not synchronized on sdf
            sdf.format();
        }
    }
}
        