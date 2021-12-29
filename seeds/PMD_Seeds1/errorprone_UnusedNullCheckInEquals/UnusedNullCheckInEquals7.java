
public class Foo {
    private static boolean isSame(Object[] a1, Object[] a2) {
        return a1 == a2 || (a1 != null && a2 != null && Arrays.equals(a1, a2));
    }
}
        