
public final class InboxContents<T> {
    @SafeVarargs
    public final InboxContents<T> conflateWith(T... values) { // false positive
        return conflateWith(ImmutableList.copyOf(values));
    }
}
public final class InboxContents2 {
    @java.lang.SafeVarargs
    public final InboxContents conflateWith(String... values) {
        return conflateWith(ImmutableList.copyOf(values));
    }
}
        