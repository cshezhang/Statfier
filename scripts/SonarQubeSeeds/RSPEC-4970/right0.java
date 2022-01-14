
public class HiddenCatchBlock {

  public static class CustomException extends Exception {
  }

  public static class CustomDerivedException extends CustomException {
  }

  public static void main(String[] args) {
    try {
      throwCustomDerivedException();
    } catch(CustomDerivedException e) { // Compliant; try-catch block is "catching" only the Exception that can be thrown in the "try"
      //...
    }
  }
}
