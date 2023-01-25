import net.jcip.annotations.Immutable;
import java.util.logging.Logger;

@Immutable
public class Test implements Serializable, TestCase {
    public static final Logger log = Logger.getAnonymousLogger();
}