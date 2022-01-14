
public class Raspberry implements Serializable {
  // ...

  public class Drupelet implements Serializable {  // Noncompliant; output may be too large
    // ...
  }
}
