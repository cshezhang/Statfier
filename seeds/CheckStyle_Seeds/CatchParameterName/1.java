public class MyTest {
  public void myTest() {
    try {
      // ...
    } catch (ArithmeticException ex) { // OK
      // ...
    } catch (ArrayIndexOutOfBoundsException ex2) { // OK
      // ...
    } catch (IOException thirdException) { // OK
      // ...
    } catch (Exception FourthException) { // violation, the initial letter
      // should be lowercase
      // ...
    }
  }
}

