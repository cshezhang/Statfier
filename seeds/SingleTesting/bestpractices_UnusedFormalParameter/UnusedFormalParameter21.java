
abstract class Base{
  abstract public int badMethod(int arg1, String arg2);
}

class Imp1 extends Base {
  @Override
  public int badMethod(int arg1, String arg2) {
    return arg2.length();
  }
}

class Imp2 extends Base {
  @Override
  public int badMethod(int arg1, String arg2) {
    return arg2.length() + arg1;
  }
}
        