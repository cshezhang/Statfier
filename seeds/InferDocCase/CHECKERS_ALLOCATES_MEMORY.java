

import codetoanalyze.java.annotation.NoAllocation;


// CHECKERS_ALLOCATES_MEMORY
class CHECKERS_ALLOCATES_MEMORY implements I {
  @NoAllocation
  void directlyAllocatingMethod() {
    new Object();
  }

  public void foo() {}

}