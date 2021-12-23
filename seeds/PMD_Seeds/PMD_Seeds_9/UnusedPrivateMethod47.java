
public class SuperClassFalsePositive {

    public void myPublicMethod() {
        throw convertToUnchecked(new MyException("Something Bad Happened"));
    }

    private IllegalArgumentException convertToUnchecked(Exception e) {
        return new IllegalArgumentException(e);
    }

    private static class MyException extends Exception {
        public MyException(String message) {
            super(message);
        }
    }
}
        