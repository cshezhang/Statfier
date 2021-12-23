
import org.w3c.dom.traversal.NodeFilter;
public class ShortTest implements NodeFilter {
    @Override
    public short acceptNode(@Nullable Node node) {
        return 0;
    }
}
        