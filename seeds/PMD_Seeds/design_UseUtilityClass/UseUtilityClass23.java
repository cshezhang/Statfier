import lombok.experimental.UtilityClass;

@UtilityClass
public class MyUtil {
  private static final int CONSTANT = 5;

  public static int addSomething(int in) {
    return in + CONSTANT;
  }
}

