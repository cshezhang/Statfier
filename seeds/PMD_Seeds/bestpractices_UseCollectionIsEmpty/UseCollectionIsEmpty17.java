
import java.util.List;

public record CollectionRecord(List<String> theList) {
    public CollectionRecord {
        if (theList.size() == 0) throw new IllegalArgumentException("empty list");
        if (theList.isEmpty()) throw new IllegalArgumentException("empty list");
    }
}
        