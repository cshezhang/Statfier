
import javax.crypto.spec.SecretKeySpec;

public class Foo {

    static private String key = "0123456789ABCDEF";

    void encrypt() {
        byte[] keyData = key.getBytes("UTF-8");

        SecretKeySpec keySpec =  new SecretKeySpec(keyData, "AES");
    }
}
        