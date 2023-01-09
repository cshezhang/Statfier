
import lombok.experimental.Delegate;

public class Foo {
    @Delegate private String bar;

    public void set(String s) {
        bar = s;
    }
}
        