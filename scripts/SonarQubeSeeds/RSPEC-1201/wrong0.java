
class MyClass {
  private int foo = 1;

  public boolean equals(MyClass o) {  // Noncompliant; does not override Object.equals(Object)
    return o != null && o.foo == this.foo;
  }

  public static void main(String[] args) {
    MyClass o1 = new MyClass();
    Object o2 = new MyClass();
    System.out.println(o1.equals(o2));  // Prints "false" because o2 an Object not a MyClass
  }
}

class MyClass2 {
  public boolean equals(MyClass2 o) {  // Ignored; `boolean equals(Object)` also present
    //..
  }

  public boolean equals(Object o) {
    //...
  }
}
