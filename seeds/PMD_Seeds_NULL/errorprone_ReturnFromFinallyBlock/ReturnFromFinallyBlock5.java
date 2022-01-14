
public class Foo {
    void printSomething() {
        try {
            System.out.println("Integers: ");
        } finally {
            Arrays.asList(0, 1, 2).map(i -> { return i + 1; }).forEach(i -> System.out.println(i));
        }
    }
}
        