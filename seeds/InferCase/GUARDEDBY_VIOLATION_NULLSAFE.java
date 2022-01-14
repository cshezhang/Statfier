class C {
  @GuardedBy("this")
  String f;

  void foo(String s) {
    f = s; // unprotected access here
  }
}