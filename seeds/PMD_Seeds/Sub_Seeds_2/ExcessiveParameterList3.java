
public class Foo {
    private Foo() {
        Object obj = new Object() {
            public void doWork(int a, int b, int c, String abc, long d, double p,
                String[] arr, int data, long in, float fl, String res) {} // 11 params
        };
    }
}
        