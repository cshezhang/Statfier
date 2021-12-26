
import lombok.experimental.Delegate;

public class Foo {
    @Delegate
    private String bar;

    public String toString() {
        return "Foo: " + bar;
    }
}
        