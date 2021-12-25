
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Foo {
    private static final Logger LOG = LoggerFactory.getLogger(Foo.class);
    public void bar() {
        try {
            new File("/text.txt");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
        }
    }
}
        