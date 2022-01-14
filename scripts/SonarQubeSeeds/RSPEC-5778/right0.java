
@Test
public void testToString() {
   Object obj = get();
   Assert.assertThrows(IndexOutOfBoundsException.class, () -> obj.toString());
}

@Test
public void testToStringTryCatchIdiom() {
  Object obj = get();
  try {
    obj.toString();
    Assert.fail("Expected an IndexOutOfBoundsException to be thrown");
  } catch (IndexOutOfBoundsException e) {
    // Test exception message...
  }
}
