
@Test
public void myTest() {
  given(foo.bar(anyInt(), eq(i1), eq(i2))).willReturn(null);
  when(foo.baz(val1, val2)).thenReturn("hi");
  doThrow(new RuntimeException()).when(foo).quux(intThat(x -> x >= 42), eq(-1));
  verify(foo).bar(eq(i1), anyInt(), eq(i2));
  ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
  verify(foo).bar(captor.capture(), any(), any());
}
