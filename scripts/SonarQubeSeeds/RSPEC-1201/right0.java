
class MyClass {
  private int foo = 1;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MyClass other = (MyClass)o;
    return this.foo == other.foo;
  }

  /* ... */
}

class MyClass2 {
  public boolean equals(MyClass2 o) {
    //..
  }

  public boolean equals(Object o) {
    //...
  }
}
