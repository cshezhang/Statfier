import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;

public class Foo {
  void encrypt() {
    byte[] iv = new byte[16];
    SecureRandom sprng = new SecureRandom();
    sprng.nextBytes(iv);
    IvParameterSpec ivs = new IvParameterSpec(ivBytes);
  }
}

