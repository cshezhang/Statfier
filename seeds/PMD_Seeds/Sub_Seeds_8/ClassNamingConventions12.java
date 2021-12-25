
// I couldn't reproduce the original failure, but we can use another regression test.
public class MyException extends RuntimeException {
    public MyException(Exception exception) {
        super(exception);
    }
}
        