public class Case4 {
  public boolean getCondition() {
    return true;
  }

  public void methodA() {
    // boolean condition = getCondition();
    if (true) {
      methodA(); // should report a warning here
    } else {
      methodA(); // And report a warning here
    }
  }
}

