
import javax.crypto.spec.SecretKeySpec;

public class Foo {

    void encrypt() {
        SecretKeySpec keySpec =  new SecretKeySpec(secureProperty.getKeyBytes(), "AES");
    }
}
        