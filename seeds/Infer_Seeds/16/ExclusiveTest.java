class ExclusiveTest {

  void linear(int x) {
    for (int i = 0; i < x; i++) {}
  }

  void call_linear_exclusive_constant(int x) {
    linear(x);
  }

  void call_linear_exclusive_linear(int x) {
    for (int i = 0; i < x; i++) {
      linear(i);
    }
  }
}

