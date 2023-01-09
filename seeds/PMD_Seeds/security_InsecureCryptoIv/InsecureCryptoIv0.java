
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;

public class Foo {

    void outOfScope() {
        byte[] ivBytes = new byte[16];
    }
    byte[] ivBytes = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, };

    void encrypt() {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
    }
}
        