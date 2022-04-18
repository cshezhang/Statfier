

try {
  throw new RuntimeException();
} catch (RuntimeException expected) {
} // ok

try {
  throw new RuntimeException();
} catch (RuntimeException ignore) {
  // no handling
} // ok

try {
  throw new RuntimeException();
} catch (RuntimeException o) {
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException ex) {
  // This is expected
} // ok
        