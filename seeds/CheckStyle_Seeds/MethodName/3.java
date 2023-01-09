

class MyClass {
  public MyClass() {} // OK
  public void MyClass() {} // violation,  method Name 'MyClass' must not
                           // equal the enclosing class name
}
        