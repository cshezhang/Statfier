
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Foo {
    private String id;

    public Foo(String id) {
        this.id = id;
    }
}
        