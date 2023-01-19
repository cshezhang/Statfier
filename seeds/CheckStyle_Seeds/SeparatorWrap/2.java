class Test3 {

  String s;

  int a, b; // violation, separator comma on same line

  public void foo(int a, int b) { // violation, separator comma on the same line
    int r, t; // OK
  }

  public void bar(int p, int q) { // OK
  }
}

