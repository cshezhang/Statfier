public class Test {
  /** Some description here. // OK */
  private void methodWithValidCommentStyle() {}

  /** Some description here // violation, the sentence must end with a proper punctuation */
  private void methodWithInvalidCommentStyle() {}
}

