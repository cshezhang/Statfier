
import java.util.function.Consumer;

public class Bar {
    void foo(int Foo) {
    }

    void bar(final int Hoo) {
    }

    {
        Consumer<String> i = (Koo) -> {
        };

        Consumer<String> q = (String KOO) -> { // that's ok
        };

        Consumer<String> qq = (final String MOO) -> { // that's ok
        };

        Consumer<String> k = (String Voo) -> {
        };

        Consumer<String> l = (final String Ooo) -> {
        };
    }
}
        