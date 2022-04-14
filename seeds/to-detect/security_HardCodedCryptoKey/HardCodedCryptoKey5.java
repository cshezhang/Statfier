
import javax.crypto.spec.SecretKeySpec;

public class Foo {
    public void testHardCodedCryptoKey(boolean tag) {
       String str;
       if (tag) {
          str = "Hardcoded Crypto Key1"; // should report a warning here
       } else {
          str = "Hardcoded Crypto Key2"; // should report a warning here
       }
       SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
    }

    void encrypt() {
        final String var0;
        var0 = "hard coded key here";
        SecretKeySpec keySpec =  new SecretKeySpec(var0.getBytes(), "AES");
    }

    void encrypt2(String prefix, String key) {
        final String var0 = prefix;
        var0 = var0 + key;
        SecretKeySpec keySpec =  new SecretKeySpec(var0.getBytes(), "AES");
    }
}
        