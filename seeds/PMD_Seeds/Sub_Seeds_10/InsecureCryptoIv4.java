
import javax.crypto.spec.IvParameterSpec;

public class Foo {

    void encrypt() {
        byte[] ivBytes = new byte[] { 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, };
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
    }
}
        