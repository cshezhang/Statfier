
import java.text.DateFormat;

public class Foo {
    private static final DateFormat sdf = new DateFormat();
    void bar() {
        synchronized(sdf) {
            sdf.format();
        }
    }
}
        