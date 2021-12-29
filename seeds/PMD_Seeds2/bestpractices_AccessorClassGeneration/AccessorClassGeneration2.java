
public class Foo3 {
    public class InnerClass {
      void method() {
        new Foo3(); //Causes generation of accessor
      }
    }
    private Foo3() {
    }
}
        