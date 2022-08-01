
public class TestFieldDeclarations {
    class Inner {
        void method1() { }
        private int field1; // violation 1
        private Inner anon = new Inner() { // violation 2 - field "anon"
            void method2() { }
            private int field2; // violation 3
        };
    }
}
        