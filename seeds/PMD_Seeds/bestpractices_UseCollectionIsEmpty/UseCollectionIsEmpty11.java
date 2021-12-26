
import java.util.ArrayList;

public class IsEmptyTest {
    public static void main(String args[]) {
        ArrayList<String> testObject = new ArrayList<String>();

        // These should be flagged
        if (testObject.size() < 1) { // line 8
            System.out.println("List is empty");
        }
        if (1 > testObject.size()) { // line 11
            System.out.println("List is empty");
        }

        // These should not be flagged
        if (testObject.size() <= 1) { // line 16
            System.out.println("List may or may not be empty");
        }
        if (1 >= testObject.size()) { // line 19
            System.out.println("List may or may not be empty");
        }

        // These should be flagged (as they are equivalent to != 0) and are not
        if (testObject.size() >= 1) { // line 24
            System.out.println("List is not empty");
        }
        if (1 <= testObject.size()) { // line 27
            System.out.println("List is not empty");
        }

        // These should not be flagged
        if (testObject.size() > 1) { // line 32
            System.out.println("List is not empty, but not all non-empty lists will trigger this");
        }
        if (1 < testObject.size()) { // line 35
            System.out.println("List is not empty, but not all non-empty lists will trigger this");
        }
        if (testObject.size() != 1) { // line 38
            System.out.println("List may or may not be empty");
        }
        if (1 != testObject.size()) { // line 41
            System.out.println("List may or may not be empty");
        }
        if (testObject.size() == 1) { // line 44
            System.out.println("List is not empty, but not all non-empty lists will trigger this");
        }
        if (1 == testObject.size()) { // line 47
            System.out.println("List is not empty, but not all non-empty lists will trigger this");
        }
    }
}
        