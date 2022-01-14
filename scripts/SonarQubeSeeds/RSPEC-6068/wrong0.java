
@Test
public void myTest() {
  given(foo.bar(eq(v1), eq(v2), eq(v3))).willReturn(null);   // Noncompliant
  when(foo.baz(eq(v4), eq(v5))).thenReturn("foo");   // Noncompliant
  doThrow(new RuntimeException()).when(foo).quux(eq(42));    // Noncompliant
  verify(foo).bar(eq(v1), eq(v2), eq(v3));   // Noncompliant
}
