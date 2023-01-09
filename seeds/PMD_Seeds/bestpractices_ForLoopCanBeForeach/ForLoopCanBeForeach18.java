
import java.util.List;

public class Test {
    private int[] hashes;
    public void foo() {
        List<String> stringList;

        this.hashes = new int[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            this.hashes[i] = stringList.get(i).hashCode();
        }
    }
}
        