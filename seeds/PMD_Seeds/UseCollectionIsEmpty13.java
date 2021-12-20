
package iter0;
import java.util.List;

public class PmdBugBait {
    public int getSize() {
        return 0;
    }

    /**
     * "this." before the method call triggers the bug
     */
    public void compareSizeToThisPointMethod(List<String> list) {
        if (list.size() < this.getSize()) {
            throw new IllegalArgumentException();
        }
    }
}
        