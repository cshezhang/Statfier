

class MyClass {
  public <T> void method1() {} // OK
  public <a> void method2() {} // violation,  name 'a' must match pattern '^[A-Z]$'
  public <K, V> void method3() {} // OK
  public <k, V> void method4() {} // violation, name 'k' must match pattern '^[A-Z]$'
}
        