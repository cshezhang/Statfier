
public class ClassWithLambda {
    void method() {
        final Runnable runnable = () -> {
            int a = 0;
        };
    }
}
        