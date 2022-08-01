



import codetoanalyze.java.annotation.IgnoreAllocations;
import codetoanalyze.java.annotation.NoAllocation;

public class NoAllocationExample {

  @NoAllocation
  void directlyAllocatingMethod() {
    new Object();
  }

  void allocates() {
    new Object();
  }

  @NoAllocation
  void indirectlyAllocatingMethod() {
    allocates();
  }

  void doesNotAllocate() {
    // does noting
  }

  @NoAllocation
  void notAllocatingMethod() {
    doesNotAllocate();
  }

  void allocatingIsFine() {
    new Object();
  }

  @NoAllocation
  void throwsException() {
    throw new RuntimeException();
  }

  @NoAllocation
  void creatingExceptionIsFine() {
    throwsException();
  }

  @NoAllocation
  void thowingAThrowableIsFine() {
    throw new AssertionError();
  }

  @IgnoreAllocations
  void acceptableAllocation() {
    new Object();
  }

  @NoAllocation
  void onlyAllocatesInAcceptableWay() {
    acceptableAllocation();
  }
}
