
import java.util.function.Consumer;

public class Bar {

    void foo(int Foo) {
    }

    void fooBar(final int FOO) { // that's ok
    }

    void bar(final int Hoo) {
    }

    {
        Consumer<String> i = (Koo) -> {
        };

        Consumer<String> k = (String Voo) -> {
        };

        Consumer<String> l = (final String Ooo) -> {
        };
    }
}
        