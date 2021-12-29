
import lombok.Data;

@Data
public class Outer {
    public class Inner {
        private String innerField;
    }

    private String outerField;

    public Outer(String outerField) {
        this.outerField = outerField;
    }
}
        