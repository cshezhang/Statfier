
public class Logger {
    private static final Logger LOGGER = new Logger();

    private interface TestLambda {
        void apply(String arg1, String arg2);
    }
    private void runTestLambda(TestLambda test) {
        test.apply("one", "two");
    }

    public void case1_no_violation() {
        runTestLambda((String a, String b) -> {
            LOGGER.debug(a);
        });
    }

    public void case2_violation() {
        runTestLambda((String a, String b) -> {
            LOGGER.debug(a + b);
        });
        runTestLambda((s1, s2) -> {LOGGER.debug(s1 + s2);});
    }
}
        