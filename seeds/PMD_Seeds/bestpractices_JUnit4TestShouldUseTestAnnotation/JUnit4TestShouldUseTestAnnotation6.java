
import java.util.function.Predicate;

public final class Whatever<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return false;
    }
}
        