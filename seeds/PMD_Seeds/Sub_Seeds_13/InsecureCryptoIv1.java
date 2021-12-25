
import javax.crypto.spec.IvParameterSpec;

public class Foo {

    void encrypt(SecretKeySpec key) {
        byte[] ivBytes = new byte[key.getEncoded().length];
        Util.getSecureRandom().nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
    }
}
        