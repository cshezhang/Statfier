
public class Test {
    public void bar() {
        // Inner Builder pattern chained
        final Bar bar = Bar.newBuilder()
            .withFoo("foo")
            .build();
    }
}
        