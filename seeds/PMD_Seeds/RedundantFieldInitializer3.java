package iter0;

public class Foo {
    java.lang.Object[] oa0;
    java.lang.Object[] oa = null; // Bad
    java.lang.Object[] oa1 = computed();

    java.lang.Object[][] oaa0;
    java.lang.Object[][] oaa = null; // Bad
    java.lang.Object[][] ooa1 = new java.lang.Object[1][];
    java.lang.Object[][] ooa2 = new java.lang.Object[][] { { null } };

    static java.lang.Object[] computed() {
        return null;
    }
}
        