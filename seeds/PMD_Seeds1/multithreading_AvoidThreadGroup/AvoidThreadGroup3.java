
public class Foo {
    void bar() {
        ThreadGroup t = System.getSecurityManager().getThreadGroup();
    }
}
        