
import java.util.Collection;
import java.util.Set;
public final class Foo<A, B extends Collection<A>> {
    private final Set<? super B> things;
}
        