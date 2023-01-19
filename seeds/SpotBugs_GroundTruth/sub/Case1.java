public class Case1 {

  public boolean s() {
    String s1 = "str1";
    final String s2 = "str2";
    return s1 == s2; // should report a warning here
  }
}

