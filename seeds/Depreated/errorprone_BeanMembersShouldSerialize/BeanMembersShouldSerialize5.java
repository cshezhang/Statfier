public class Foo {
  private String foo;
  private String bar = Foo.foo;

  public void setFoo(Foo foo) {
    this.foo = foo;
  }

  public String getFoo() {
    return foo;
  }
}

