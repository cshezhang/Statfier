
class BaseClass {  // Noncompliant; should implement Cloneable
  @Override
  public Object clone() throws CloneNotSupportedException {    // Noncompliant; should return the super.clone() instance
    return new BaseClass();
  }
}

class DerivedClass extends BaseClass implements Cloneable {
  /* Does not override clone() */

  public void sayHello() {
    System.out.println("Hello, world!");
  }
}

class Application {
  public static void main(String[] args) throws Exception {
    DerivedClass instance = new DerivedClass();
    ((DerivedClass) instance.clone()).sayHello();              // Throws a ClassCastException because invariant #2 is violated
  }
}
