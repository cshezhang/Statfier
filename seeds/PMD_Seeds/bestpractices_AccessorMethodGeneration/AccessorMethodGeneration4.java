
public class Foo {
    public class InnerClass {
        private void secret() {
            outerSecret(); // violation, accessing a private method
        }
    }

    private void outerSecret() {
    }
}
        