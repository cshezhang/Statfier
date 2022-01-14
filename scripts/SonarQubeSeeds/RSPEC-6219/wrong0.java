
record Person(String name, int age) implements Serializable {
@Serial
  private static final long serialVersionUID = 0L; // Noncompliant
}
