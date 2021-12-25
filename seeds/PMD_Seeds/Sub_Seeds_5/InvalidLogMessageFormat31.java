
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        String string0 = "a";
        String string1 = "b";
        LOGGER.trace("first line {}"
                + "second line {}",
                string0,
                string1);
        String string2 = "c";
        LOGGER.debug("first line {} "
                + "second line {} and "
                + "the third line {}.",
                string0, string1, string2);
    }
}
        