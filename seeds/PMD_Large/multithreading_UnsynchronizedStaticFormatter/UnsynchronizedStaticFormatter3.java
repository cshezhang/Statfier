
import java.text.SimpleDateFormat;

public class Foo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    public void bar() {
        synchronized (sdf) {
            sdf.format();
        }
    }
}
        