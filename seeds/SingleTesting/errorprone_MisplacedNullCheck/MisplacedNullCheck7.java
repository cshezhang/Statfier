
public class Test {
    String field;

    Test(String f) { field = f; }

    public boolean method(final String value) {
        boolean c = value != null
             && (field == null || (field != null && !value.equals(field.toString())));
        return c;
    }
}
        