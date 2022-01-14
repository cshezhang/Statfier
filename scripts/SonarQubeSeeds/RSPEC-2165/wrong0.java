
public class Foo {
  private String name;

  @Override
  void finalize() {
    name = null;  // Noncompliant; completely unnecessary
  }
}
