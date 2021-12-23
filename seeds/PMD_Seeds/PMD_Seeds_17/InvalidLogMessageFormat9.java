
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBug1551
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestBug1551.class);

    public void test()
    {
        String message = generateMessage();
        LOGGER.info(message);
    }

    private String generateMessage()
    {
        return "message";
    }
}
        