class A {

  int len(@Nullable String s) {
    if (s != null) {
      return s.length();
    } else {
      return 0;
    }
  }
}

class B extends A {

  int len(String s) {  // @Nullable missing.
    return s.length();
  }
}

public class Main {

    String s;
  
    int foo() {
      A a = new B();
      return a.len(s);
    }
  }