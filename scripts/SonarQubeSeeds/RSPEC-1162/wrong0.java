
public void myMethod1() throws CheckedException {
  ...
  throw new CheckedException(message);   // Noncompliant
  ...
  throw new IllegalArgumentException(message); // Compliant; IllegalArgumentException is unchecked
}

public void myMethod2() throws CheckedException {  // Compliant; propagation allowed
  myMethod1();
}
