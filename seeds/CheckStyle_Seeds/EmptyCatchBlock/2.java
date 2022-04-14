

try {
  throw new RuntimeException();
} catch (RuntimeException expected) {
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException ignore) {
  // no handling
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException o) {
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException ex) {
  // This is expected
} // ok
        