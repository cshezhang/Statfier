
@Test(expected = IndexOutOfBoundsException.class)
public void testShouldFail() {
  get();
  // This test pass since execution will never get past this line.
  Assert.assertEquals(0, 1);
}

private Object get() {
  throw new IndexOutOfBoundsException();
}
