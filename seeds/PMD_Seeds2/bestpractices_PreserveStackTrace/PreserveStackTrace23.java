
public class Test {
    public void foo() {
        try {
            // do something
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            NoSuchElementException noSuchElementException = new NoSuchElementException(
                    "Cannot return next element, because there is none!");
            noSuchElementException.initCause(arrayIndexOutOfBoundsException);
            throw noSuchElementException;
        }

        try {
            // do something
        } catch (ArrayOutOfBoundsException e) {
            throw (IllegalStateException)new IllegalStateException().initCause(e);
        }
    }
}
        