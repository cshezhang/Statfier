
Boolean b = getBoolean();
if (b) {  // Noncompliant, it will throw NPE when b == null
  foo();
} else {
  bar();
}
