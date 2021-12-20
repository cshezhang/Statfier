package iter0;

public class ObjectCreator {
    public Supplier<String> create() {
        final java.lang.Object o = new java.lang.Object(); // captured by the method ref
        return o::toString;
    }
}
        