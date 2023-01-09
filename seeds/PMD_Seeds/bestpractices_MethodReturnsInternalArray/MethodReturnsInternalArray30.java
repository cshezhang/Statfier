
public class Outer {
    private static final String[] names = new String[] {"a", "b"};
    public static Provider getProvider() {
        return new Provider() {
            @Override
            public String[] getNames() {
                return names;
            }
        };
    }
    public interface Provider {
        String[] getNames();
    }
}
        