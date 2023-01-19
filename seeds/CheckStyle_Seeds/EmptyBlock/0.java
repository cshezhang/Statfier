public class Test {
  private void emptyLoop() {
    for (int i = 0; i < 10; i++) { // violation
    }

    try { // violation

    } catch (Exception e) {
      // ignored
    }
  }
}

