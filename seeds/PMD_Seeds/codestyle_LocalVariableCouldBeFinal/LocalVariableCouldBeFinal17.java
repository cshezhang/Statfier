
public class ClassWithAnon {
    void method() {
        final ClassWithAnon anon = new ClassWithAnon() {
            void bar() {
                String a = "a";
                System.out.println(a);
            }
        };
        final Runnable runnable = new Runnable() {
            public void run() {
                int a = 0;
            }
        };
    }
}
        