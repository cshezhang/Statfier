
public class Pomegranate {
  // ...

  public class Seed implements Serializable {  // Noncompliant; serialization will fail
    // ...
  }
}
