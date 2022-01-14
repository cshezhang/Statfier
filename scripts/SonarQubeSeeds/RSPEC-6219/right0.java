
record Person(String name, int age) implements Serializable {} // Compliant

record Person(String name, int age) implements Serializable {
@Serial
  private static final long serialVersionUID = 42L; // Compliant
}
