
public void doSomething(String str) {
  if (Math.abs(str.hashCode()) > 0) { // Noncompliant
    // ...
  }
}
