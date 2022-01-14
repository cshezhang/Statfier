
try {
  byte[] bytes = string.getBytes("UTF-8"); // Noncompliant; use a String instead of StandardCharsets.UTF_8
} catch (UnsupportedEncodingException e) {
  throw new AssertionError(e);
}
// ...
byte[] bytes = string.getBytes(Charsets.UTF_8); // Noncompliant; Guava way obsolete since JDK7
