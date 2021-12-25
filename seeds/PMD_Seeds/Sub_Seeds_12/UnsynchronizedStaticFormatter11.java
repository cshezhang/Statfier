
import java.text.SimpleDateFormat;

public class Foo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    synchronized void bar() { // not static
        sdf.format();
    }
}
        