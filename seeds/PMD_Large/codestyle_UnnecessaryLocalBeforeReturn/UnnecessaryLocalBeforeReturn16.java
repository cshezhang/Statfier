
public class ObjectCreator {
    public Supplier<String> create() {
        final Object o = new Object(); // captured by the method ref
        return o::toString;
    }
}
        