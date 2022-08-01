
public class Foo {
    private static List buz;
    private static boolean b = false;
    public static List bar(String foo) {
        if (buz == null) {
            buz = new ArrayList();
            if (foo == null) {
                b = true;
            }
        }
        return buz;
    }
}
        