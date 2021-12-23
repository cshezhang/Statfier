
public class Foo {

    public boolean dummyMethod(final String captured, final Set<String> dummySet) {
        captured = captured.trim();
        return dummySet.stream().noneMatch(value -> value.equalsIgnoreCase(captured));
    }

}        