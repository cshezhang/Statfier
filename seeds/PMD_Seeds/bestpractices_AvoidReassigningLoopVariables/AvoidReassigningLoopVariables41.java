public interface InterfaceWithClass {
  class Inner {
    void foo(int bar) {
      for (int i = 0; i < 10; i++) {
        doSomethingWith(i);
        i = 5; // not OK
      }
    }
  }
}

