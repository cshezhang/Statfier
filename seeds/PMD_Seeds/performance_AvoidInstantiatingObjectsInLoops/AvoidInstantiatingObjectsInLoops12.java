
public class Sample {
    private static class Inner {
        private void baz() {
            for(;;) {
                String a = new String();
            }
        }
    }

    public void foo() {
        Sample anonymousClass = new Sample() {
            void bar() {
                for(;;) {
                    String a = new String();
                }
            }
        };
    }
}
        