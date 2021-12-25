
public class Foo {
    private int field;

    public class InnerClass {
        private long innerField;
    }

    long method() {
        return new InnerClass().innerField; // violation, accessing a private field
    }
}
        