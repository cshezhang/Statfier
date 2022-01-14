
class StringUtils { // Compliant

  private StringUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static String concatenate(String s1, String s2) {
    return s1 + s2;
  }

}
