
public class Foo {
    private static List list;
    public static List bar(String param) {
        if (list == null || !param.equals("foo")) {
            list = new ArrayList();
            param = "x";
        }
        return list;
    }
}
        