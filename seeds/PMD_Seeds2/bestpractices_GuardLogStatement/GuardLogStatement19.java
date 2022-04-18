
public class Logger {
    private static final Logger LOGGER = new Logger();

    public void bar() {
        LOGGER.info(() -> "Bla " + " bla"); // The lambda is free to do whatever it likes
    }

    @Override
    public void info(Supplier<String> message) {
        if (logger.isInfoEnabled()) {
            logger.info(message.get());
        }
    }
}
        