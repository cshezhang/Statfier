

try {
  throw new RuntimeException();
} catch (RuntimeException e) {
  //This is expected
}
...
try {
  throw new RuntimeException();
} catch (RuntimeException e) {
  //   This is expected
}
...
try {
  throw new RuntimeException();
} catch (RuntimeException e) {
  // This is expected
  // some another comment
}
...
try {
  throw new RuntimeException();
} catch (RuntimeException e) {
  /* This is expected */
}
...
try {
  throw new RuntimeException();
} catch (RuntimeException e) {
  /*
  *
  * This is expected
  * some another comment
  */
}
...
try {
  throw new RuntimeException();
} catch (RuntimeException myException) {

}
        