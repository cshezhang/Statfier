
Assert.assertTrue(true);  // Noncompliant
assertThat(null).isNull(); // Noncompliant

assertEquals(true, something()); // Noncompliant
assertNotEquals(null, something()); // Noncompliant
