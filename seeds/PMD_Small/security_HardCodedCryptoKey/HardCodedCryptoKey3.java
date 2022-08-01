
import javax.crypto.spec.SecretKeySpec;

public class Foo {

    static private byte[] encryptionKey = new byte[] { 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 };

    void encrypt() {
        SecretKeySpec keySpec =  new SecretKeySpec(encryptionKey, "AES");
    }
}
        