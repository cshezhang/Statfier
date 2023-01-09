
public class Hello {
  private void greet(String name) {
    name = name.trim();
    System.out.println("Hello " + name);

    // preferred
    String trimmedName = name.trim();
    System.out.println("Hello " + trimmedName);
  }
}
        