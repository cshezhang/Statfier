
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class InvalidLog4j2ExceptionTest {

    private static final Logger LOGGER = LogManager.getLogger(InvalidLog4j2ExceptionTest.class);

    public void foo() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            try {
                LOGGER.error("Exception was thrown during conversion from tc model: {}.", jsonMarshaller.marshall(tcmport));
            } catch (IOException e1) {
                LOGGER.error("Problem to marshall to json tcImport: {}", tcsTripImport, e1);
            }
            throw e;
        }
    }
}
        