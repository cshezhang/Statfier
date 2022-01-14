
int f(Object o) {
  if (o instanceof String) {  // Noncompliant
    String string = (String) o;
    return string.length();
  }
  return 0;
}
