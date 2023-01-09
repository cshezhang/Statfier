
import java.util.ArrayList;

public class TestIsEmpty {
    public static void main(String args[]) {
        ArrayList<String> testObject = new ArrayList<String>();

        // These should be flagged
        if (testObject.size() == 0) {
            System.out.println("List is empty");
        }
        if (testObject.size() != 0) {
            System.out.println("List is empty");
        }
        if (0 == testObject.size()) {
            System.out.println("List is empty");
        }
        if (0 != testObject.size()) {
            System.out.println("List is empty");
        }
        if (testObject.size() > 0) {
            System.out.println("List is empty");
        }
        if (testObject.size() < 1) {
            System.out.println("List is empty");
        }
        if (0 < testObject.size()) {
            System.out.println("List is empty");
        }
        if (1 > testObject.size()) {
            System.out.println("List is empty");
        }
        if (new ArrayList().size() == 0) {
            System.out.println("New list starts empty");
        }
        if (GetArrayList().size() == 0) {
            System.out.println("List returned from function is empty");
        }

        // These should not be flagged
        if (GetBar().size() == 0) {
            System.out.println("This should not be flagged as it is not a list");
        }
    }
    public static ArrayList<String> GetArrayList() {
        return new ArrayList<String>();
    }
    public static SomeOtherObjectWithSizeMethod GetBar() {
        return new SomeOtherObjectWithSizeMethod();
    }
}
        