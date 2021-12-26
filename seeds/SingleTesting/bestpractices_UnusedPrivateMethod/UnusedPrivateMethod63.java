
import lombok.val;

public class ValExample {
    public void example() {
        val example = "value";
        aPrivateMethod(example);
    }

    private void aPrivateMethod(String s) {
        System.out.println(s);
    }
}
        