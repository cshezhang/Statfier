package MutatorTestingCases;

import javax.crypto.spec.SecretKeySpec;

public class Case4 {

    public static final String globalHardcodedKey = "hardcoded key";

    public void testHardCodedCryptoKey(String s) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(globalHardcodedKey.getBytes(), "AES");
        System.out.println(secretKeySpec.getAlgorithm());
    }
}
