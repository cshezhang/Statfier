
public class PrivateConstructorClass {  // Noncompliant
  private PrivateConstructorClass() {
    // ...
  }

  public static int magic(){
    return 42;
  }
}
