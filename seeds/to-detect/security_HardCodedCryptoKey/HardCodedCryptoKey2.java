import javax.crypto.spec.SecretKeySpec;

public class Foo {

  private static String key = "0123456789ABCDEF";

  void encrypt() {
    byte[] keyData = key.getBytes("UTF-8");

    SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
  }
}

