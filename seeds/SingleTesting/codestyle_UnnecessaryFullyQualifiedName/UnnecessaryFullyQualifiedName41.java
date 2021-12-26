

public class FQNTest {
    public static void main(String[] args) {
        length length = new length();
        // the type name 'length' is obscured.
        int i = length.foo;
    }
}

class length {
    static int foo;
}
        