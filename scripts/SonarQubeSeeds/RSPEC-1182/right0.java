
class BaseClass implements Cloneable {
  @Override
  public Object clone() throws CloneNotSupportedException {    // Compliant
    return super.clone();
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
    ((DerivedClass) instance.clone()).sayHello();              // Displays "Hello, world!" as expected. Invariant #2 is satisfied
  }
}
