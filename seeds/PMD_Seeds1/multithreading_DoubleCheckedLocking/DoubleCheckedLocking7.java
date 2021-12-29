
public class Foo {
    private static volatile Foo instance;

    public static Foo getInstance() {
        Foo result = instance;
        if (result == null) {
            synchronized (Foo.class) {
                result = instance;
                if (result == null) {
                    result = instance = new Foo();
                }
            }
        }
        return result;
    }
}
        