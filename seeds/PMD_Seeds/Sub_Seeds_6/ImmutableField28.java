
import lombok.experimental.Delegate;

public class Foo {
    @Delegate
    private String x;

    public Foo() {
        x = "bar";
    }
}
        