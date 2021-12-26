
public class PatternMatchingInstanceof {
    private String s = "other string";

    public void test() {
        Object obj = "abc";
        if (obj instanceof String s) {
            System.out.println("a) obj == s: " + (obj == s)); // true
        }
    }
}
        