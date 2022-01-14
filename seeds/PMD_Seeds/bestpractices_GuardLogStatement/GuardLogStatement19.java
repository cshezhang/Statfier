
public class Logger {
    private static final Logger LOGGER = new Logger();

    public void bar() {
        String msg;
        msg = "test";
        LOGGER.info(() -> "Bla " + msg + " bla"); // The lambda is free to do whatever it likes
    }

    @Override
    public void info(Supplier<String> message) {
        if (logger.isInfoEnabled()) {
            logger.info(message.get());
        }
    }
}
        