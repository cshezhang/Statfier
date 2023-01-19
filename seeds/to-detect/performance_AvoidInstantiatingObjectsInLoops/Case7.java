package MutatorTestingCases;


public class Case7 {

  static class Foo {}

  public void test() {
    for (int i = 0; i < 10; i++) {
      Foo f = new Foo();
    }
    // ArrayList<Object> strs = new ArrayList<>();
    // strs.forEach(str->{
    //     Foo foo = new Foo();  // should report a warning here
    // });
  }
}

