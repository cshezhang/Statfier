
import javax.crypto.spec.IvParameterSpec;

public class Foo {

    String getLiteral18() {
        return "ALL_ZEROS_HERE";
    }

    void encrypt() {
        byte[] staticIv = getLiteral18().getBytes();
        IvParameterSpec iv = new IvParameterSpec(staticIv);
    }
}
