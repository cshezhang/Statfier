// CHECKERS_CALLS_EXPENSIVE_METHOD
class CHECKERS_CALLS_EXPENSIVE_METHOD {

  @PerformanceCritical
  void perfCritical() {
    expensive();
  }

  @Expensive
  void expensive() {}
}