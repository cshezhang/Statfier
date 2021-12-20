package iter0;

public class Test {
    public void bar() {
        // Builder pattern
        final Builder builder = Builder.newBuilder();
        builder.withFoo();
        final FooBuilder fooBuilder = FooBuilder.newBuilder();
        fooBuilder.withBar();
    }
}
        