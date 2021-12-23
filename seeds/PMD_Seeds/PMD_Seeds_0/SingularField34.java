
/** Dummy class. */
public final class Test {
    private Timer timer1;
    private Timer timer2;

    /** Dummy constructor. */
    public Timer() {
        this.timer1 = new Timer(0, e -> {
            // do nothing for now
        });
        this.timer2 = new Timer(0, e -> {
            // do nothing for now
        });
    }

    /** Use a lambda expression to reference timer1 -- triggers SingularField error. */
    private final Runnable play1 = () -> {
        this.timer1.start();
    };

    /** Use an anonymous class to reference timer2 -- no error. */
    private final Runnable play2 = new Runnable() {
        @Override
        public void run() {
            this.timer2.start();
        }
    };
}
        