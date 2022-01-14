
public boolean isConditionMet(int a, int b) {
  int difference = a - b;

  if (difference < 0) {
    return false;
  }

  // ...

  MyClass foo = new MyClass(a);
  if (foo.doTheThing()) {
    return true;
  }
  return false;
}
