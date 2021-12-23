
import java.util.ArrayList;
import java.util.List;
public class Foo {
    List<String> field;
    public void foo() {
       List<String> strings = new ArrayList<String>();
       List<String> strings2 = new ArrayList<>();
       List<List<String>> strings3 = new ArrayList<>();
       List<List<String>> strings4 = new ArrayList<List<List<String>>>();
       this.field = new ArrayList<String>();
    }
}
        