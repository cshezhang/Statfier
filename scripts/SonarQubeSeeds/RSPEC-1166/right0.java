
try {
  /* ... */
} catch (Exception e) {
  LOGGER.info(e);  // exception is logged
}

try {
  /* ... */
} catch (Exception e) {
  throw new RuntimeException(e);   // exception stack trace is propagated
}

try {
  /* ... */
} catch (RuntimeException e) {
  doSomething();
  throw e;  // original exception passed forward
} catch (Exception e) {
  throw new RuntimeException(e);  // Conversion into unchecked exception is also allowed
}
