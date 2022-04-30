public class Case8 {
    public static final boolean tag = false;

    public void foo() {
        if (tag) {
            System.gc();
        }
    }
}