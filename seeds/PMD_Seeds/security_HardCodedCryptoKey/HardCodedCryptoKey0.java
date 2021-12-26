
import javax.crypto.spec.SecretKeySpec;

public class Foo {

    void encrypt() {
        SecretKeySpec keySpec =  new SecretKeySpec("hard coded key here".getBytes("UTF-8"), "AES");
    }
}
        