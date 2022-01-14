class ERADICATE_RETURN_OVER_ANNOTATED {
  @Nullable String m() {
    String s = new String("abc");
    return s;
  }
}