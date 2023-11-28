import javax.annotation.Nullable;

public class DoubleExample {

  @Nullable Double x;

  private Double testAssignNonNullOk() {
    x = 1.0;
    return x + 1.0;
  }

  private Double testdReadNullableBad() {
    return x + 1.0;
  }
}

