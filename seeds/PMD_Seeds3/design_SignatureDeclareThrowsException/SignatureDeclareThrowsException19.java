
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public final class Namespace {

    @Target(ElementType.TYPE_USE)
    public @interface Weird {
    }

    public class WeirdException extends @Weird Exception {
    }

}
        