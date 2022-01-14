
public boolean isConditionMet(int a, int b) {
  int difference = a - b;
  MyClass foo = new MyClass(a);  // Noncompliant; not used before early return

  if (difference < 0) {
    return false;
  }

  // ...

  if (foo.doTheThing()) {
    return true;
  }
  return false;
}
