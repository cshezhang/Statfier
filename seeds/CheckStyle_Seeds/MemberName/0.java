class MyClass {
  public int num1; // OK
  protected int num2; // OK
  final int num3 = 3; // OK
  private int num4; // OK

  static int num5; // ignored: not an instance variable
  public static final int CONSTANT = 1; // ignored: not an instance variable

  public int NUM1; // violation, name 'NUM1'
  // must match pattern '^[a-z][a-zA-Z0-9]*$'
  protected int NUM2; // violation, name 'NUM2'
  // must match pattern '^[a-z][a-zA-Z0-9]*$'
  final int NUM3; // violation, name 'NUM3'
  // must match pattern '^[a-z][a-zA-Z0-9]*$'
  private int NUM4; // violation, name 'NUM4'
  // must match pattern '^[a-z][a-zA-Z0-9]*$'
}

