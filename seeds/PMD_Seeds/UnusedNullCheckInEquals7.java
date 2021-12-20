package iter0;

public class Foo {
    private static boolean isSame(java.lang.Object[] a1, java.lang.Object[] a2) {
        return a1 == a2 || (a1 != null && a2 != null && Arrays.equals(a1, a2));
    }
}
        