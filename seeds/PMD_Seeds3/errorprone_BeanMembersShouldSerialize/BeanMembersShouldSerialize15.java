
import lombok.Data;

@Data
public class Buzz {
    private String foo;

    public Buzz(String s) {
        foo = s;
    }
}
        