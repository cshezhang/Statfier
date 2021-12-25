
@SuppressWarnings("unused")
class Outer {
    private static final String CONST_STRING = "value";
    private static final String CALCULATED_STRING = "value" + 0;
    private static final int LITERAL = 0;
    private static final int CALCULATED = LITERAL * 2;
    private static final int CAST = (int) 0L;
    private static final long NON_CONSTANT = java.util.concurrent.TimeUnit.SECONDS.toMillis(10);
    private static final String NON_CONSTANT_STR = NON_CONSTANT + "foo";
    private static final int LATE_INIT;
    private static String STATIC_STRING = "value";
    static {
        LATE_INIT = 0;
    }

    class Inner {
        @Override
        public String toString() {
        return "" // separate lines so the rule violations show up better
               + CONST_STRING
               + CALCULATED_STRING
               + LITERAL
               + CALCULATED
               + CAST
               + NON_CONSTANT     // valid violation
               + NON_CONSTANT_STR // valid violation
               + LATE_INIT        // valid violation
               + STATIC_STRING    // valid violation
            ;
        }
    }
}
        