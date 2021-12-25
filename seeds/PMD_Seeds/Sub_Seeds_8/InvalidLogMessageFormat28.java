
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TestBug1551
{
    private static final Logger LOGGER = LogManager.getLogger(TestBug1551.class);

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
        