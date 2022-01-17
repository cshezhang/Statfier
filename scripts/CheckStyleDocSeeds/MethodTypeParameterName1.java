

class MyClass {
  public <T> void method1() {} // OK
  public <a> void method2() {} // OK
  public <K, V> void method3() {} // OK
  public <k, V> void method4() {} // OK
}
        