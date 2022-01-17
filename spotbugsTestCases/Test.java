import java.util.ArrayList;

public class Test {
    ArrayList<Integer> lst = new ArrayList<Integer>();
    Integer[] res;
    public Integer[] asArray() {
        res = (Integer[]) ((Object[])(lst.toArray()));  // should report a warning here
        return this.res;
    }
}