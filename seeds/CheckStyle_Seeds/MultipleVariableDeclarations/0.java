

public class Test {
  public void myTest() {
    int mid;
    int high;
    // ...

    int lower, higher; // violation
    // ...

    int value,
        index; // violation
    // ...

    int place = mid, number = high;  // violation
  }
}
        