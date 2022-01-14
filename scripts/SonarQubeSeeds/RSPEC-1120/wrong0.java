
class Foo {
  public int a;
   public int b;   // Noncompliant, expected to start at column 4

...

  public void doSomething() {
    if(something) {
          doSomethingElse();  // Noncompliant, expected to start at column 6
  }   // Noncompliant, expected to start at column 4
  }
}
