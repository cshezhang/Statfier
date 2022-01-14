
int f(Object o) {
  if (o instanceof String) {  // Compliant
    return 42;
  }
  return 0;
}

int f(Number n) {
  if (n instanceof String) {  // Compile-time error
    return 42;
  }
  return 0;
}

boolean fun(Object o, String c) throws ClassNotFoundException
{
  return Class.forName(c).isInstance(o); // Compliant, can't use instanceof operator here
}
