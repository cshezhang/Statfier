// CHECKERS_ALLOCATES_MEMORY
class C implements I {
  @NoAllocation
  void directlyAllocatingMethod() {
    new Object();
  }
}