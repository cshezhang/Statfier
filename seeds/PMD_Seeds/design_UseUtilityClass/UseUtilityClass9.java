
public class UseSingleton extends Exception {
    UseSingleton(final String string) {
        super(foo(string));
    }

    private static String foo(final String string) {
        return string;
    }
}
        