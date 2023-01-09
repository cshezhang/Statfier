
public class TestFieldDeclarations {
    class Inner {
        void method1() { }
        private int field1; // violation 1
        private Inner anon = new Inner() { // ignored due to ignoreAnonymousClassDeclarations
            void method2() { }
            private int field2; // violation 2
        };
    }
}
        