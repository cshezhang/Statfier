
public class Foo {
    public void foo(String a) {
        try {
            int i = Integer.parseInt(a);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
        