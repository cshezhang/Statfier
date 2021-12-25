
import lombok.Data;

public class Outer {
    @Data
    public class Inner {
        private String innerField;
    }

    private String outerField;

    public Outer(String outerField) {
        this.outerField = outerField;
    }
}
        