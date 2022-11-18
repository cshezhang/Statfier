import java.util.ArrayList;

public class Foo {
    public void test() {
        ArrayList<Object> strs = new ArrayList<>();
        for(int i = 0; i < strs.size(); i++) {
            Foo foo = new Foo();  // should report a warning here
        }
        strs.forEach(str->{
            Foo foo = new Foo();  // should report a warning here
        });
    }
}