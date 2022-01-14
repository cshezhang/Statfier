
// This test correctly fails.
@Test
public void testToString() {
    Object obj = get();
    Assert.assertThrows(IndexOutOfBoundsException.class, () -> obj.toString());
    Assert.assertEquals(0, 1);
}
