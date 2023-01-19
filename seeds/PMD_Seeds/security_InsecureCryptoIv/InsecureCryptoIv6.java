import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class Foo {
  void encrypt() {
    byte[] iv = new byte[16];
    SecureRandom sprng = new SecureRandom();
    sprng.nextBytes(iv);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
  }
}

