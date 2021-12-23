
import java.util.function.Predicate;

public class ClassWithPredicates {
    private final Predicate<String> isNotEmpty = string -> !string.isEmpty();
}
        