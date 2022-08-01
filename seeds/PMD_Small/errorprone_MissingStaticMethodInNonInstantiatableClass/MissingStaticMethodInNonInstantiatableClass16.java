
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.PersistenceConstructor;

public class Foo {

    private String arg;

    @PersistenceConstructor
    private Foo() {}

    @Autowired
    private Foo(String arg) {
        this.arg = arg;
    }

    public void bar() {}
}
        