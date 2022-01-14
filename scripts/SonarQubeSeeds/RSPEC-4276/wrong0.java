
public class Foo implements Supplier<Integer> {  // Noncompliant
    @Override
    public Integer get() {
      // ...
    }
}
