
public class Foo {
    void myMethod() {
        Object anonymous = new Object() {
            public void bar1() {
                int y = 1;
                return;
            }
        };
        class Local {
            public void bar2() {
                int y = 2;
                return;
            }
        }
    }
    class Inner {
        void bar3() {
            int y = 3;
            return;
        }
    }
}
interface MyInterface {
    default void myDefaultMethod() {
        int y = 4;
        return;
    }
}
        