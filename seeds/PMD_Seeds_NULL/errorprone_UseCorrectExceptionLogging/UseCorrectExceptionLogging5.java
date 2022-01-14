
public class UseCorrectExceptionLoggingCase {
    protected static final Log logger1 = LogFactory.getLog(DISCONNECTED_CLIENT_LOG_CATEGORY);
    protected final Log logger2 = LogFactory.getLog(getClass());

    public void run() {
        try {
            //...
        } catch (Exception e) {
            logger2.debug("Error", e);
        }
    }
}
        