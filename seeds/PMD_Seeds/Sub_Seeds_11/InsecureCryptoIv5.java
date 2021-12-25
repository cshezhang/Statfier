
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class Foo {
    void encrypt() {
        byte[] iv = new byte[16];
        SecureRandom sprng = new SecureRandom();
        sprng.nextBytes(iv);
        IvParameterSpec ivs = new IvParameterSpec(ivBytes);
    }
}
     