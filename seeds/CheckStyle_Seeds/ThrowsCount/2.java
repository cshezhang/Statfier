class Test {
  public void myFunction()
      throws CloneNotSupportedException, ArrayIndexOutOfBoundsException,
          StringIndexOutOfBoundsException, IllegalStateException,
          NullPointerException { // violation, max allowed is 4
    // body
  }

  public void myFunc() throws ArithmeticException, NumberFormatException { // ok
    // body
  }

  private void privateFunc()
      throws CloneNotSupportedException, ClassNotFoundException, IllegalAccessException,
          ArithmeticException, ClassCastException { // violation, max allowed is 4
    // body
  }

  private void func() throws IllegalStateException, NullPointerException { // ok
    // body
  }
}

