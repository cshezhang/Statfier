
import static java.lang.Integer.*;
import static java.lang.Long.valueOf;
import static java.lang.Long.*;

public class Foo {
    public void foo() {
        // the "valueOf" method is both in Integer.* and Long.*
        // we need an explicit static import to specify one of them, e.g. Long.valueOf
        valueOf("123", 10);

        // covered by Integer.*
        int i = parseInt("123");

        // covered by Long.*
        long l = parseLong("123");
    }
}
     