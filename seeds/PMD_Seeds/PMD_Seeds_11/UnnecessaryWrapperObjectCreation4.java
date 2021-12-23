
public class Test {
    public void test() {
        Float.valueOf(5).toString(); // this will raise the rule
        Float.toString(5); // this is the preferred way
    }
}
        