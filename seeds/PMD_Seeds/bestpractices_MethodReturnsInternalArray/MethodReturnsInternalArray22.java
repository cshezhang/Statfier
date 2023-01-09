
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
public final class I {
    private I() {
    }
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Inner {
        String[] value();
    }
}
        