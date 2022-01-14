class C {
  void m(C x) {
    String s = x.toString()
  }

  void test(@Nullable C x) {
    m(x);
  }
}