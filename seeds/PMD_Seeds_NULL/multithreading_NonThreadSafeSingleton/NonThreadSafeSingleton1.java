
public class Foo {
    private static List buz;
    public static synchronized List bar() {
        if (buz == null) buz = new ArrayList();
        return buz;
    }
}
        