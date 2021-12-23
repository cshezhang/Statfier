
public class Foo {
    Object baz;
    Object bar() {
        if(baz == null) { //baz may be non-null yet not fully created
            synchronized(this) {
                if(baz == null) {
                    baz = new Object();
                }
            }
        }
        return baz;
    }
}
        