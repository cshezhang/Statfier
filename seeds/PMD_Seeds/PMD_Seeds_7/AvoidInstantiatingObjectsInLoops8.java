
import java.awt.Dimension;

public class PMDDemo {
    public static void main(final String[] args) {
        final Dimension[] arr = new Dimension[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Dimension(i, i); // rule violation here
        }
    }
}
        