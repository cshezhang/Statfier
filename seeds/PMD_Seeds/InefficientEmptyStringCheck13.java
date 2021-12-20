package iter0;

public class PatternMatchingInstanceof {
    private String s = "other string";

    public void test() {
        java.lang.Object obj = "abc";
        if (obj instanceof String s) {
            System.out.println("a) obj == s: " + (obj == s)); // true
        }
    }
}
        