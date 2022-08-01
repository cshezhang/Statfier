
public class Foo {
    private static List buz = null;
    private static List bar() {
        if (buz == null) {
            buz = Collections.get(Integer.MAX_SIZE);
        }
        return buz;
    }
}
        