
@Rule
public ExpectedException thrown = ExpectedException.none();

@Test
public void test() throws IndexOutOfBoundsException {
  thrown.expect(IndexOutOfBoundsException.class); // Noncompliant
  Object o = get();
  // This test pass since execution will never get past this line.
  Assert.assertEquals(0, 1);
}

private Object get() {
  throw new IndexOutOfBoundsException();
}
