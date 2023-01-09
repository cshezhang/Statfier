
import java.util.Map;
import java.util.Collection;
import java.util.function.Consumer;

public class FalsePositive {

    public void fp1(Map<Class<?>, Consumer<?>> consumerMap, final StringBuilder builder) {
        consumerMap.put(Collection.class, o -> {
            builder.append('[');
            Collection<?> collection = (Collection<?>) o;
            collection.forEach(t -> builder.append(t));
            builder.append(']'); // here reported (with pmd 6.22.0)
        });

        consumerMap.put(Map.class, o -> {
            builder.append('{');
            Map<?, ?> map = (Map<?, ?>) o;
            map.forEach((k, v) -> builder.append(k).append('=').append(v));
            builder.append('}');
        });
    }
}
        