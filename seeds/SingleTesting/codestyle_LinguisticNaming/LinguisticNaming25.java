
import java.util.function.Predicate;

public class SomeClass {
    public Predicate<String> isEmptyPredicate() {
        return String::isEmpty;
    }
}
        