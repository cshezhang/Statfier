
public class Foo {
    private static Locale locale;
    public static List bar() {
        if (locale==null) return Locale.getDefault();
        return locale;
    }
}
        