
public class Test {
    public static void foo() {
        synchronized(Test.class) {
            // only a block is synchronized on Test.class
        }
    }
}
        