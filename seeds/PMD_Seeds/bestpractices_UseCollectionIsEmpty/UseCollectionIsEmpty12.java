
import java.util.Collection;

public class PMDIsEmptyFalsePositive {
    public void falsePositive() {
        Collection<String> c = new ArrayList<String>();

        if (c.size() <= 1) // false positive
        {
            // do something
        }
    }
}
        