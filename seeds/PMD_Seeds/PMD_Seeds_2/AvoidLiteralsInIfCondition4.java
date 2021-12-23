
public class Foo {
    public void bar() {
        if (aDouble > 0.0) {}                  // magic number 0.0
        if (aDouble >= Double.MIN_VALUE) {}    // preferred approach
    }
}
        