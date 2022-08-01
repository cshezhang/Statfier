
import java.util.stream.Stream;

public class Foo {
    void bar() {
        Stream<T> stream = Stream.of(2);
        if (condition) {
            stream = stream.skip(2);
        }
    }
}
        