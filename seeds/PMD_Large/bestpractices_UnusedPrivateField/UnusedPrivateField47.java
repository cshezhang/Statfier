
public class Foo {
    private static final String OUTER_CONSTANT = "";

    private static final class Empty {
        private static final class Inner {
            public boolean isEmpty(String s) {
                return OUTER_CONSTANT.equals(s);
            }
        }
    }
}
        