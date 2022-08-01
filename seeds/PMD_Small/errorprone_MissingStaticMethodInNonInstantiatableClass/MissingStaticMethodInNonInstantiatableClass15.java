
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;

public class Foo {

    private String arg;

    @Autowired
    private Foo() {}

    @Inject
    private Foo(String arg) {
        this.arg = arg;
    }

    public void bar() {}
}
        