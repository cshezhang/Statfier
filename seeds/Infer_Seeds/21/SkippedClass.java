// _SHOULD_BE_SKIPPED_

public class SkippedClass {

  Object f;

  public static SkippedClass returnOwned() {
    return new SkippedClass();
  }
}

