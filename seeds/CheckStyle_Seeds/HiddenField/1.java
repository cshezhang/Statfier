public class SomeClass {

  private String field;
  private String testField;

  public SomeClass(String testField) { // OK, 'testField' param doesn't hide any field
  }

  public void method(String param) { // OK
    String field = param; // violation, 'field' variable hides 'field' field
  }

  public void setTestField(String testField) { // OK, 'testField' param doesn't hide any field
    this.field = field;
  }

  public SomeClass setField(String field) { // OK, 'field' param doesn't hide any field
    this.field = field;
  }
}

