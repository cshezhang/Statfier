public class Test {
  private void emptyLoop() {
    for (int i = 0; i < 10; i++) {
      // ignored
    }

    // violation on next line
    try {

    } catch (Exception e) {
      // ignored
    }
  }
}

