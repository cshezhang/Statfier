
import lombok.Getter;

@Getter
public enum Foo {
    BAR(1);

    private final int number;

    Foo(final int number) {
        this.number = number;
    }
}
        