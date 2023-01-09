
public class Test {
    public void foo() {
        try {
            // do something
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            NoSuchElementException noSuchElementException = new NoSuchElementException(
                    "Cannot return next element, because there is none!");
            noSuchElementException.initCause(new RuntimeException("some other unrelated exception"));
            throw noSuchElementException;
        }
    }
}
        