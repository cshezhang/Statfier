abstract class SomeClass {

  private String field;

  public SomeClass(int field) { // violation, 'field' param hides a 'field' field
    float field; // violation, 'field' variable hides a 'field' field
  }

  public abstract int method(String field); // OK
}

public class Demo extends SomeClass {

  public int method(String param) {
    return param;
  }
}

