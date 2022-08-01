
public class Foo {
    Object baz;
    Object bar() {
        if (baz == null) { //baz may be non-null yet not fully created
            synchronized(this) {
                if (null == baz) {
                    baz = new Object();
                }
            }
        }
        return baz;
    }
}
        