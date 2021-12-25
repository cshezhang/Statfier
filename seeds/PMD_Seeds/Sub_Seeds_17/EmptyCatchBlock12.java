
public class Foo {
    void foo() {
        try {
        } catch (NullPointerException expected) {
        } catch (IllegalArgumentException ignored) {
        }
    }
}
        