
Assert.assertThrows(IndexOutOfBoundsException.class, () -> get());
// This test correctly fails.
Assert.assertEquals(0, 1);
