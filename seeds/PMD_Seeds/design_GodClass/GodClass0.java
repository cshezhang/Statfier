public class Foreigner {
  public int data;
  private double other;

  public void getData() {
    return data;
  }

  public void setData(int data) {
    this.data = data;
  }

  public double getOther() {
    return other;
  }

  public void setOther(double other) {
    this.other = other;
  }

  public void someOtherNonDataMethod() {}
}

public class Foo {
  int myown = 2;

  private int somePrivateFunction() {
    if (myown == 2) {
      return 42;
    } else {
      return myown;
    }
  }

  private int anotherFunction() {
    // used, to have the TCC lower than 0.333 - this method
    // doesn't use myown
  }

  public void first() {
    Foreigner f = new Foreigner();
    int a = 1;
    int b = 2;
    int c = somePrivateFunction();

    if (a == 2) {
      b = a - 1;
    }

    f.setData(3);
    f.data = 5;
    myown = f.getData();
    f.someOtherNonDataMethod();
  }

  public void other() {
    int b = 100;
    for (int i = 0; i < 2; i++) {
      b++;
    }

    if (b < 10) {
      Foreigner f2;
      f2.setOther(1.0);
      if (f2.getOther() == 1.0) {
        while (b < 10) {
          b++;
        }
      }
      if (f2.data == 5) {
        f2.setData(4);
      }
    }

    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
    if (true || false || true || false || true || false) {
      if (b == 1 || b == 2 || b == 3 || b == 4 || b == 5) {
        if (b == 1 && b == 2 && b == 3 && b == 4 && b == 5) {}
      }
    }
  }
}

