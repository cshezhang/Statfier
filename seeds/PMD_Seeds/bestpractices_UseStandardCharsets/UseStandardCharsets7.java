
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Foo {
    public static void charset() {
         // looking up the charset dynamically
        try (OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            osw.write("test");
        }
    }
}
        