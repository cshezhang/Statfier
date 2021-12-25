
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidSlf4jExceptionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidSlf4jExceptionTest.class);

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
        