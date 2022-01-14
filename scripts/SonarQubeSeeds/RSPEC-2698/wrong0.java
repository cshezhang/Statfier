
assertEquals(4, list.size());  // Noncompliant

try {
  fail();  // Noncompliant
} catch (Exception e) {
  assertThat(list.get(0)).isEqualTo("pear");  // Noncompliant
}
