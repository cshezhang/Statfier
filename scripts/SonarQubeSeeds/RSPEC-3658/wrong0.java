
@Test
public void testMethod() {
  try {
            // Some code
  } catch (MyException e) {
    Assert.fail(e.getMessage());  // Noncompliant
  }
}
