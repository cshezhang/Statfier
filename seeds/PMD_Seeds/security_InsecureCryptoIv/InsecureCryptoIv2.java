
import javax.crypto.spec.IvParameterSpec;

public class Foo {

    void encrypt() {
        byte[] staticIv = "ALL_ZEROS_HERE".getBytes();
        IvParameterSpec iv = new IvParameterSpec(staticIv);
    }
}
     