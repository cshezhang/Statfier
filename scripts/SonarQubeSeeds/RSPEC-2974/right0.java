
public final class PrivateConstructorClass {  // Compliant
  private PrivateConstructorClass() {
    // ...
  }

  public static int magic(){
    return 42;
  }
}
