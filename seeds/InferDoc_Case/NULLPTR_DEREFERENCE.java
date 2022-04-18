import static com.google.common.base.Preconditions.checkNotNull;

public class NULLPTR_DEREFERENCE {
    public Object foo() {
        return null;
    }
    public void stuff() {}
    public void foo1(){
        Object p = foo(); // foo() might return null
        stuff();
        p.toString();   // dereferencing p, potential NPE
    }


    public void foo2(){
        //... intervening code
        Object p =checkNotNull(foo()); // foo() might return null
        stuff();
        p.toString(); // p cannot be null here
    }
}