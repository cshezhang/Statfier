
class Vegetable {  // does not implement Serializable
  private transient Season ripe;  // Noncompliant
  // ...
}
