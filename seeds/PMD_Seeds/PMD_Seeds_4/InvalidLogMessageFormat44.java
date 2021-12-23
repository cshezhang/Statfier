
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestInvalidLogMessageFormat {
    Logger log = LoggerFactory.getLogger(TestInvalidLogMessageFormat.class);

    public void testPMD() {
        /* whistled, fp*/ log.debug("param %10s %% %n %%", "1"); // string formatted message

        /*10:violation*/ log.debug("param %.2d%s", "expected 2 params, given 1");
        try {
            throw new Exception();
        } catch (Exception e) {
            /*14:violation*/ log.debug("param %s", "expected 1 params, given 2", "too many params", e);
        }
    }

    protected void logProblem(String type, Object val) {
        if (log.isDebugEnabled() && val instanceof Throwable) {
            /*whistled, fp*/ log.debug("Trace for "+type+" reading "+getBriefDescription()+": "+val, (Throwable)val);
        }

        if (log.isDebugEnabled()) {
            /*whistled, fp*/ log.debug("Recurring {} reading {} in {} (still in grace period): {}", new Object[] {type, this, getBriefDescription(), val});
        }
    }

    private String getBriefDescription() { return ""; }
}
        