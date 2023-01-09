

class Foo{ // violation, file contains less than 2 occurrences of "public"
  private int a;
  /* public comment */ // OK, comment is ignored
  private void bar1() {}
  public  void bar2() {} // violation
}
        