
public class Test {
    public void method(final String value,final String oldValue) {
        if ((value != null && !value.equals(oldValue)) || value == null) {
            // Do something
        }

        if ((value == null || !value.equals(oldValue)) && value != null) {}
    }
}
        