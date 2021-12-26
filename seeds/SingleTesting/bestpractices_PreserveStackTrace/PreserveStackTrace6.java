
public class Foo {
    public void foo(String a) {
        try {
            int i = Integer.parseInt(a);
        } catch(Exception e){
            throw new Exception(Bar.foo(e),e);
        }
    }
}
        