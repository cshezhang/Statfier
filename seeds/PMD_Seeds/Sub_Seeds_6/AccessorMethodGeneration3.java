
public class Foo {
    public int publicField;
    protected int protectedField;
    /* package */ int packageField;

    public class InnerClass {
        private long innerField;

        InnerClass() {
            innerField = Foo.this.publicField; // this is ok
            innerField += Foo.this.protectedField; // this is ok
            innerField += Foo.this.packageField; // this is ok
        }
    }
}
        