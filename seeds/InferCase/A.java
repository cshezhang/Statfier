// CHECKERS_EXPENSIVE_OVERRIDES_UNANNOTATED
interface I {
  void foo();
}

class A implements I {
  @Expensive
  public void foo() {}
}