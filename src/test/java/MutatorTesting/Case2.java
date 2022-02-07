package MutatorTestingCases;

import javax.crypto.spec.SecretKeySpec;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/10/13 3:51 下午
 */
public class Case2 {

    public void testHardCodedCryptoKey() {
        String str = "Hardcoded Secret Key";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
        System.out.println(secretKeySpec.getAlgorithm());
    }
}
