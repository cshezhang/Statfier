
public class Foo {
    public class InnerClass {
        private void secret() {
            outerPublic(); // this is ok
            outerProtected(); // this is ok
            outerPackage(); // this is ok
        }
    }

    public void outerPublic() {
    }

    protected void outerProtected() {
    }

    /* package */ void outerPackage() {
    }
}
        