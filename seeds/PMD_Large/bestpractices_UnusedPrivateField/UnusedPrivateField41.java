
import java.io.InputStream;

public class IssueUnusedPrivateField {

    private InputStream is;

    public IssueUnusedPrivateField(InputStream is) {
        this.is = is;
    }

    public void testSomething() {
        try (is) {
            System.out.println("foo!");
        }
    }
}
        