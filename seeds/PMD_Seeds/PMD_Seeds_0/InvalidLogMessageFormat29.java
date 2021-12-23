
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Foo
{
    private static final Logger LOGGER = LogManager.getLogger(Foo.class);

    public void test()
    {
        int attempt = 0;
        LOGGER.info("test (attempt #{})", ++attempt);
    }
}
        