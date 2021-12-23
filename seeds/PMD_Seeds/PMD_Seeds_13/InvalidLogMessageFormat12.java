
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
        