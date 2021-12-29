
public class Foo {
    Object[] oa0;
    Object[] oa = null; // Bad
    Object[] oa1 = computed();

    Object[][] oaa0;
    Object[][] oaa = null; // Bad
    Object[][] ooa1 = new Object[1][];
    Object[][] ooa2 = new Object[][] { { null } };

    static Object[] computed() {
        return null;
    }
}
        