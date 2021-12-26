
public enum Testing {
    Test;

    public void test(ITesting tester) {
        tester.test();
    }
    public static interface ITesting {
        void test();
    }
}
        