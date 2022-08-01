

class MyClass {
  void method1(int v1) {} // OK
  void method2(int V2) {} // violation, name 'V2' must match pattern '^[a-z][a-zA-Z0-9]*$'
}
        