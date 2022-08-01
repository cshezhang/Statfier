public class BC_IMPOSSIBLE_DOWNCAST_OF_TOARRAY {
    String[] getAsArray(Collection<String> c) {
        return (String[]) c.toArray();
    }
}