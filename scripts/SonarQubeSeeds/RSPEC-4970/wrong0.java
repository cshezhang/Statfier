
public class HiddenCatchBlock {

  public static class CustomException extends Exception {
  }

  public static class CustomDerivedException extends CustomException {
  }

  public static void main(String[] args) {
    try {
      throwCustomDerivedException();
    } catch(CustomDerivedException e) {
      // ...
    } catch(CustomException e) { // Noncompliant; this code is unreachable
      // ...
    }
  }

  private static void throwCustomDerivedException() throws CustomDerivedException {
    throw new CustomDerivedException();
  }
}
