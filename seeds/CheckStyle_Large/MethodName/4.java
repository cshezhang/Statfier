

class MyClass {
  public void FirstMethod() {} // OK
  protected void SecondMethod() {} // OK
  private void ThirdMethod() {} // violation, name 'ThirdMethod' must match
                                // pattern '^[a-z](_?[a-zA-Z0-9]+)*$'
  void FourthMethod() {} // violation, name 'FourthMethod' must match
                         // pattern '^[a-z](_?[a-zA-Z0-9]+)*$'
}
        