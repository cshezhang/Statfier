
public class Logger {
    private static final Logger LOGGER = new Logger();

    public void bar() {
        LOGGER.info("Message with param: {}", expensiveOperation()); // bad, expensiveOperation is executed regardless
        LOGGER.info("Message with param: %s", this.expensiveOperation()); // bad, expensiveOperation is executed regardless
        LOGGER.info(expensiveOperation()); // bad, expensiveOperation is executed regardless

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Message with param: {}", expensiveOperation());
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Message with param: %s", expensiveOperation());
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(expensiveOperation());
        }

        LOGGER.info("Message with param: {}", () -> expensiveOperation());
        LOGGER.info("Message with param: %s", () -> expensiveOperation());
        LOGGER.info(()-> expensiveOperation());
    }

    private String expensiveOperation() {
        return "...";
    }
}
        