class ERADICATE_CONDITION_REDUNDANT {
  void m() {
    String s = new String("abc");
    if (s != null) {
      int n = s.length();
    }
  }
}