package iter0;

public class ObjectCreator {

    private static final String A = "";
    private static final String B = "" + A; // the existence of this line causes the NPE.

    public java.lang.Object create() {
        final java.lang.Object o = new java.lang.Object(A);
        return o;
    }
}
        