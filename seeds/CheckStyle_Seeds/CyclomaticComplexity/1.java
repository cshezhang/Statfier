

class CyclomaticComplexity {
  // Cyclomatic Complexity = 5
  int a, b, c, d;
  public void foo() { // 1, function declaration
    while (a < b // 2, while
      && a > c) {
      fun();
    }
    if (a == b) {
      do { // 3, do
        fun();
      } while (d);
    } else if (c == d) {
      while (c > 0) { // 4, while
        fun();
      }
      do { // 5, do-while
        fun();
      } while (a);
    }
  }
}
        