

class MyClass {
  void method1(int v1) {} // OK
  protected method2(int V2) {} // violation, Parameter name 'V2'
                               // must match pattern '^[a-z]([a-z0-9][a-zA-Z0-9]*)?$'
  private method3(int a) {} // OK
  public method4(int b) {} // violation, Parameter name 'b'
                           // must match pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'
}
        