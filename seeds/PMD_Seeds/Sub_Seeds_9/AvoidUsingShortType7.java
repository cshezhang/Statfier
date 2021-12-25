
public class ShortTest {
    public org.w3c.dom.traversal.NodeFilter create() {
        return new org.w3c.dom.traversal.NodeFilter() {
            @Override
            public short acceptNode(@Nullable Node node) {
                return 0;
            }
        };
    }
}
        