

import codetoanalyze.java.annotation.Expensive;

// CHECKERS_EXPENSIVE_OVERRIDES_UNANNOTATED
interface I {
  void foo();
}

class CHECKERS_EXPENSIVE_OVERRIDES_UNANNOTATED implements I {
  @Expensive
  public void foo() {}
}