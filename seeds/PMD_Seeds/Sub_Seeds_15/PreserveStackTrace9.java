
public class Foo {
    public void foo(String a) {
        try {
            int i = Integer.parseInt(a);
        } catch(Exception e10){
            Exception e1 = new Exception(e10);
            throw e1;
        }
    }
}
        