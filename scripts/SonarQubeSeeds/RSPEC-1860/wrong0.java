
private static final Boolean bLock = Boolean.FALSE;
private static final Integer iLock = Integer.valueOf(0);
private static final String sLock = "LOCK";
private static final List<String> listLock = List.of("a", "b", "c", "d");

public void doSomething() {

  synchronized(bLock) {  // Noncompliant
    // ...
  }
  synchronized(iLock) {  // Noncompliant
    // ...
  }
  synchronized(sLock) {  // Noncompliant
    // ...
  }
  synchronized(listLock) {  // Noncompliant
    // ...
  }
