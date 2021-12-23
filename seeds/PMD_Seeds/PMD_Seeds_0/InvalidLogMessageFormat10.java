
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foo
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Foo.class);

    public void test()
    {
        int attempt = 0;
        LOGGER.info("test (attempt #{})", ++attempt);
    }
}
        