

try {
  throw new RuntimeException();
} catch (RuntimeException expected) {
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException ignore) {
  // no handling
} // ok, catch block has comment

try {
  throw new RuntimeException();
} catch (RuntimeException o) {
} // violation

try {
  throw new RuntimeException();
} catch (RuntimeException ex) {
  // This is expected
} // ok
        