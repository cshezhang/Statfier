package iter0;

public class InnerPrivateFieldInAnotherInner {
    static class InnerUsing {
        int method() {
            return InnerDeclaring.INNER_FIELD;
        }
    }
    static class InnerDeclaring {
        private static int INNER_FIELD;
    }
}
        