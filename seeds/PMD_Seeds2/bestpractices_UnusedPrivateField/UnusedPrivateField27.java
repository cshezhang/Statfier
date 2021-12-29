
public class InnerPrivateFieldCall {
    int method() {
        return Inner.FIELD.length();
    }
    static class Inner {
        private static final String FIELD = "";
    }
}
        