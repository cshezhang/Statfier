public class RV_EXCEPTION_NOT_THROWN {

  public void false_positive() {
    if (x < 0) {
      new IllegalArgumentException("x must be nonnegative");
    }
  }

  public void true_positive() {
    if (x < 0) {
      throw new IllegalArgumentException("x must be nonnegative");
    }
  }
}

