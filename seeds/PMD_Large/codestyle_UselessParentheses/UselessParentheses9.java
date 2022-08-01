
public class Foo {
    private final boolean isModifier(int mask) {
        return (modifiers & mask) == mask;
    }
}
        