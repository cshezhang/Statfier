

public class SomeClass {

  private String field;
  private String testField;

  public SomeClass(String testField) { // OK, 'testField' param doesn't hide any field
  }
  public void method(String param) { // OK
      String field = param; // violation, 'field' variable hides 'field' field
  }
  public void setTestField(String testField) { // violation, 'testField' variable
                                               // hides 'testField' field
      this.field = field;
  }
  public SomeClass setField(String field) { // violation, 'field' param hides 'field' field
      this.field = field;
  }
}
        