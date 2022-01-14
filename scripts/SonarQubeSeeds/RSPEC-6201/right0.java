
int f(Object o) {
  if (o instanceof String string) {  // Compliant
    return string.length();
  }
  return 0;
}
