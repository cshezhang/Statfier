
import java.util.Date;

public class Foo {
    Object o0;
    Object o = null; // Bad
    static Object sto = null; // Bad

    String str0;
    String str = null; // Bad
    static String ststr = null; // Bad

    private String sameLine = null, sameLine1 = null; // 2 Bad
    private String sameLine2, sameLine3 = null; // Bad

    Date d = new Date(null);

    static Object computed() {
        return null;
    }
}
        