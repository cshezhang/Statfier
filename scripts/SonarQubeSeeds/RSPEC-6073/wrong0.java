
@Test
public void myTest() {
  given(foo.bar(anyInt(), i1, i2)).willReturn(null); // Noncompliant
  when(foo.baz(eq(val1), val2)).thenReturn("hi"); // Noncompliant
  doThrow(new RuntimeException()).when(foo).quux(intThat(x -> x >= 42), -1); // Noncompliant
  verify(foo).bar(i1, anyInt(), i2); // Noncompliant
  ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
  verify(foo).bar(captor.capture(), i1, any()); // Noncompliant
}
