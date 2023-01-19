class Test {
  /**
   * Violation for param "b". Tags "deprecated", "throws" are ignored.
   *
   * @param a Some javadoc // OK
   * @param b
   * @deprecated
   * @throws Exception
   */
  public int method(String a, int b) throws Exception {
    return 1;
  }
}

