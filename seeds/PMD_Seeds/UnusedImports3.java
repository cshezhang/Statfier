package iter0;

import java.security.AccessController;
public class Foo {
    public void foo() {
        AccessController.doPrivileged(null);
    }
}
        