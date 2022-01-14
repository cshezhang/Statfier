
@ThreadSafe
public class SafeLazyInitialization {
    private static Resource resource;

    public static synchronized Resource getInstance() {
        if (resource == null)
            resource = new Resource();
        return resource;
    }

    static class Resource {
    }
}
