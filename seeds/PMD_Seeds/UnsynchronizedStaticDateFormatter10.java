package iter0;

import java.text.SimpleDateFormat;

public class Foo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    static void bar() {
        synchronized(sdf) {
            sdf.format();
        }
    }
}
        