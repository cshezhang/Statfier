
public class MyException extends RuntimeException {

    private static final long serialVersionUID = -8134636876462178354L;


    public MyException(String message, Throwable cause) {
        super(message, cause);
    }


    public MyException(String message) {
        super(message);
    }

}
        