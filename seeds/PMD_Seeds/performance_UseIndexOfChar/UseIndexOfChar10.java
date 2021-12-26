
public class Foo {
    public void bar() {
        Optional<Service> optionalResult = null;
        services.stream()
            .filter(s -> s.getLastSeen() > 0)
            .findFirst();
    }
}
        