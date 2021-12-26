
import java.util.Arrays;
public enum MethodReturnsInternalArrayCaseEnum {
    ONE("One"),
    TWO("Two", "Three");

    private String[] titles;

    MethodReturnsInternalArrayCaseEnum(String... titles) {
        this.titles = Arrays.copyOf(titles, titles.length);
    }

    public String[] getTitles() {
        return titles.clone();
    }

    @Override
    public String toString() {
        return titles[0];
    }
}
        