public class Case3 {
    @Nonnull
    static Object obj; // should report a warning because it is not initialized.
    static {
        obj = obj;
    }
}