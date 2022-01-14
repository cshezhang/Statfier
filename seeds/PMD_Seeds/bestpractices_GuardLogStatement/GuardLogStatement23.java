
public class Logger {
    private static final Logger LOGGER = new Logger();
    private static final String CONSTANT = "a constant";

    public void case1() {
        LOGGER.debug("log something" + " and " + "concat strings"); // the same as "log something and concat strings"
    }

    public void case2() {
        final String constantPrefix = "log something";
        final String constantSuffix = "concat strings";
        LOGGER.debug(constantPrefix + " and " + constantSuffix ); // the same as "log something and concat strings"
    }

    public void case3() {
        LOGGER.debug("Prefix " + CONSTANT);
    }

    public void case4() {
        LOGGER.debug("Some long string that has been " + 
            "separated into two constants. Contains value: {}", param);
    }

    private void log(int line, String key) {
        System.out.println(line + ":" + key);
    }

    public void case5(int line, String key) {
        log(line, key);
        LOGGER.debug(key);
    }

    private interface TestLambda {
        void apply(String arg1, String arg2);
    }
    private void runTestLambda(TestLambda test) {
        test.apply("one", "two");
    }
}
        