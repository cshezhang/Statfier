
int f(Object o) {
  if (String.class.isInstance(o)) {  // Noncompliant
    return 42;
  }
  return 0;
}

int f(Number n) {
  if (String.class.isInstance(n)) {  // Noncompliant
    return 42;
  }
  return 0;
}
