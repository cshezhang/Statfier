

public class SomeClass {

  private String field;
  private String testField;

  public SomeClass(String testField) { // violation, 'testField' param hides 'testField' field
  }
  public void method(String param) { // OK
      String field = param; // violation, 'field' variable hides 'field' field
  }
  public void setTestField(String testField) { // OK, 'testField' param doesn't hide any field
      this.field = field;
  }
  public SomeClass setField(String field) { // violation, 'field' param hides 'field' field
      this.field = field;
  }
}
        