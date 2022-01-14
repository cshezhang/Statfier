
@Test
public void myTest() {
  given(foo.bar(v1, v2, v3)).willReturn(null);
  when(foo.baz(v4, v5)).thenReturn("foo");
  doThrow(new RuntimeException()).when(foo).quux(42);
  verify(foo).bar(v1, v2, v3);
}
