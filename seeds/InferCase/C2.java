// CHECKERS_CALLS_EXPENSIVE_METHOD
class C {
  @PerformanceCritical
  void perfCritical() {
    expensive();
  }

  @Expensive
  void expensive() {}
}