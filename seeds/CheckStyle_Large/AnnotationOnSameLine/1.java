

@Deprecated interface Foo {  // OK

  void doSomething();

}

class Bar implements Foo {

  @SuppressWarnings("deprecation")  // violation, annotation should be on the same line
  public Bar() {
  }

  @Override  // OK
  public void doSomething() {
  }

  @Nullable  // violation, annotation should be on the same line
  String s;

}
        