
import javax.crypto.spec.IvParameterSpec;

public class Foo {

    byte[] ivBytes = new byte[] { 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, };

    void encrypt() {
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
    }
}
     