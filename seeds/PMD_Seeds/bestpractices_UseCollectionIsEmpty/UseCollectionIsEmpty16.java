
import java.util.List;

public enum ComponentSize {

    S("s");

    private List<String> list;
    private String size;

    ComponentSize(String size) {
        if (list.size() == 0) {
            this.size = size;
        }
    }

    @Override
    public String toString() {
        return size;
    }

}
        