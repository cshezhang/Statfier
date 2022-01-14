
public class FooTest { // Noncompliant: Mockito initialization missing
  @Mock private Bar bar;

  @Spy private Baz baz;

  @InjectMocks private Foo fooUnderTest;

  @Test
  void someTest() {
    // test something ...
  }

  @Nested
  public class Nested {
    @Mock
    private Bar bar;
  }
