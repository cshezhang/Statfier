
public class Foo {
    private int field;

    public class InnerClass {
        private long innerField;

        InnerClass() {
            innerField = Foo.this.field; // violation, accessing a private field
        }
    }
}
        