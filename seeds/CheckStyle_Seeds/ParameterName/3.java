

class MyClass {
  void method1(int v1) {} // OK
  void method2(int v_2) {} // violation, name 'v_2' must match pattern '^[a-z][a-zA-Z0-9]+$'
  void method3(int V3) {} // violation, name 'V3' must match pattern '^[a-z][a-zA-Z0-9]+$'
}
        