
import org.springframework.data.annotation.PersistenceConstructor;

public class Foo {

    private String arg;

    @PersistenceConstructor
    private Foo() {}

    @PersistenceConstructor
    private Foo(String arg) {
        this.arg = arg;
    }

    public void bar() {}
}
        