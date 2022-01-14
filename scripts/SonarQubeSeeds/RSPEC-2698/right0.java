
assertEquals("There should have been 4 Fruits in the list", 4, list.size());

try {
  fail("And exception is expected here");
} catch (Exception e) {
  assertThat(list.get(0)).as("check first element").overridingErrorMessage("The first element should be a pear, not a %s", list.get(0)).isEqualTo("pear");
}
