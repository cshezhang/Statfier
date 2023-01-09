
public class Foo {
    public static class Outer {
        private int field;

        public class Inner {
            private long innerField;

            Inner() {
                innerField = Outer.this.field; // violation, accessing a private field
            }
        }
    }
}
        