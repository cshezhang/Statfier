
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 2854498759784815062L;

    public MyException() {
        super(foo());
    }

    protected static String foo() {
        return "foo";
    }
}
        