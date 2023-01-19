class A {

  public Object clone() { // OK
    return super.clone();
  }
}

class B {
  private int b;

  public B clone() { // violation, does not call super.clone()
    B other = new B();
    other.b = this.b;
    return other;
  }
}

class C {

  public C clone() { // OK
    return (C) super.clone();
  }
}

