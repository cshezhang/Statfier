package iter0;

import java.text.SimpleDateFormat;

public class Foo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    synchronized void bar() { // should be static
        sdf.format();
    }
}
        