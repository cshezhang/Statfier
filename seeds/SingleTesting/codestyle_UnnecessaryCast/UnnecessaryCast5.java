
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapCasts {
    private final Map<Class<?>, Map<String, ?>> resourceCaches = new ConcurrentHashMap<>(4);

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getResourceCache(Class<T> valueType) {
        return (Map<String, T>) this.resourceCaches.computeIfAbsent(valueType, key -> new ConcurrentHashMap<>());
    }
}
        