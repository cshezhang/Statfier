
public class ObjectCreator {

    private static final String A = "";
    private static final String B = "" + A; // the existence of this line causes the NPE.

    public Object create() {
        final Object o = new Object(A);
        return o;
    }
}
        