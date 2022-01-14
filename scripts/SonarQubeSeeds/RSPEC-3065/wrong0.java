
  private static final int UPPER = 20;
  private static final int LOWER = 0;

  public int doRangeCheck(int num) {    // Let's say num = 12
    int result = Math.min(LOWER, num);  // result = 0
    return Math.max(UPPER, result);     // Noncompliant; result is now 20: even though 12 was in the range
  }
