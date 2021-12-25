
import java.text.DateFormat;

public class Foo {
    private static final DateFormat sdf = new DateFormat();
    synchronized void bar() {
        sdf.format();
    }
}
        