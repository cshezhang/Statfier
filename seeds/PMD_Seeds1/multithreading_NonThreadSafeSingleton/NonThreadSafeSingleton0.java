
public class Foo {
    private static List buz;
    public static List bar() {
        if (buz == null) buz = new ArrayList();
        return buz;
    }
}
        