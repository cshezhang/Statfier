

class MyClass {
  public int NUM1; // OK
  protected int NUM2; // violation, name 'NUM2'
                      // must match pattern '^[a-z][a-zA-Z0-9]*$'
  int NUM3; // violation, name 'NUM3'
            // must match pattern '^[a-z][a-zA-Z0-9]*$'
  private int NUM4; // OK
}
        