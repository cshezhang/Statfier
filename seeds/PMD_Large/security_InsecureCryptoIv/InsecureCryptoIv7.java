
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import javax.crypto.Cipher;

public class Foo {
    void encrypt() {
        byte[] iv; // no direct initialization
        iv = new byte[16];
        SecureRandom sprng = new SecureRandom();
        sprng.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
    }
}
        