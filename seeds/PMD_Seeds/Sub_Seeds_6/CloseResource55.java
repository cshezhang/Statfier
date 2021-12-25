
import java.io.*;
import java.util.function.Supplier;
public class Foo {
    public Supplier<Integer> bar() throws IOException {
        InputStream inputStream = new FileInputStream("/test.txt");
        return () -> {
            try {
                return inputStream.read();
            } finally {
                inputStream.close();
            }
        };
    }
}
        