import javax.annotation.Nonnull;

public class Case11 {

    @Nonnull
    static Object obj;  // should report a warning because it is not initialized.
    static {
        obj = obj;
    }

}
