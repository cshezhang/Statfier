
@Test
public void testToString() {
  // Do you expect get() or toString() throwing the exception?
  org.junit.Assert.assertThrows(IndexOutOfBoundsException.class, () -> get().toString());
}

@Test
public void testToStringTryCatchIdiom() {
  try {
    // Do you expect get() or toString() throwing the exception?
    get().toString();
    Assert.fail("Expected an IndexOutOfBoundsException to be thrown");
  } catch (IndexOutOfBoundsException e) {
    // Test exception message...
  }
}
